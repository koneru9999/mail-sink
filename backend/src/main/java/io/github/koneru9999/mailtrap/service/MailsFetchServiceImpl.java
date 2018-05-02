package io.github.koneru9999.mailtrap.service;

import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import io.github.koneru9999.mailtrap.controller.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class MailsFetchServiceImpl implements MailsFetchService {
    private final GreenMail smtpServer;

    @Autowired
    public MailsFetchServiceImpl(GreenMail smtpServer) {
        this.smtpServer = smtpServer;
    }

    @Override
    public Flux<Message> fetchEmails(Integer pageNumber, Integer pageSize) {
        List<MimeMessage> emails = Arrays.asList(this.smtpServer.getReceivedMessages());
        long toSkip = pageNumber * pageSize;
        long take = pageNumber * pageSize > emails.size() ? emails.size() % pageSize : pageSize;
        return Flux.fromIterable(emails)
                .sort((msg1, msg2) -> {
                    try {
                        return msg1.getSentDate().getTime() > msg2.getSentDate().getTime() ? -1 : 1;
                    } catch (MessagingException e) {
                        log.error(e.getMessage(), e);
                    }
                    return -1;
                })
                .skip(toSkip)
                .take(take)
                .map(this::transform)
                .doOnError((error) -> log.error(error.getMessage(), error))
                .doOnComplete(() -> log.info("returned {} records for page {}", take, pageNumber));
    }

    @Override
    public Mono<Void> clearMessages() {
        return Mono.create(x -> {
            try {
                this.smtpServer.purgeEmailFromAllMailboxes();
                x.success();
            } catch (FolderException e) {
                x.error(e);
            }
        }).then()
                .doOnSuccess((viod) -> log.info("Cleared all messages"))
                .doOnError((error) -> log.error(error.getMessage(), error));
    }

    @Override
    public Mono<Message> getById(String messageId) {
        return Flux.fromArray(this.smtpServer.getReceivedMessages())
                .filter((x) -> {
                    try {
                        return x.getMessageID().equalsIgnoreCase(messageId);
                    } catch (MessagingException e) {
                        log.error(e.getMessage(), e);
                    }
                    return false;
                })
                .single()
                .map(this::transform)
                .doOnSuccess((success) -> log.info("returned message {}", messageId))
                .doOnError((error) -> log.error(error.getMessage(), error))
                .onErrorReturn(null);
    }

    @Override
    public Mono<Integer> count() {
        return Mono.just(this.smtpServer.getReceivedMessages().length)
                .doOnSuccess((success) -> log.info("returned count {}", success))
                .doOnError((error) -> log.error(error.getMessage(), error));
    }

    /**
     * @param mimeMessage
     * @return
     */
    private Message transform(MimeMessage mimeMessage) {
        try {
            Message message = new Message();
            message.setBody(mimeMessage.getContent().toString());
            message.setSubject(mimeMessage.getSubject());
            message.setFrom(mimeMessage.getFrom()[0]);
            message.setSent(mimeMessage.getSentDate());
            message.setTo(mimeMessage.getRecipients(javax.mail.Message.RecipientType.TO));
            message.setCc(mimeMessage.getRecipients(javax.mail.Message.RecipientType.CC));
            message.setMessageId(mimeMessage.getMessageID());
            message.setContentId(mimeMessage.getContentID());
            message.setContentType(mimeMessage.getContentType());
            return message;
        } catch (MessagingException | IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}