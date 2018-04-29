package io.github.koneru9999.mailtrap.service;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import io.github.koneru9999.mailtrap.controller.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class MailsFetchServiceImpl implements MailsFetchService {
    private static final String pattern = "EEE, dd MMM yyyy HH:mm:ss Z";
    private final SimpleSmtpServer smtpServer;

    @Autowired
    public MailsFetchServiceImpl(SimpleSmtpServer smtpServer) {
        this.smtpServer = smtpServer;
    }

    @Override
    public Flux<Message> fetchEmails(Integer pageNumber, Integer pageSize) {
        AtomicInteger ai = new AtomicInteger();
        return Flux.fromIterable( this.smtpServer.getReceivedEmails())
                .filter(
                        x -> {
                            int i = ai.incrementAndGet();
                            return i > (pageNumber * pageSize)
                                    && i <= ((pageNumber * pageSize) + pageSize);
                        }
                )
                .map(this::transform);
    }

    @Override
    public Mono<Void> clearMessages() {
        return Mono.create(x -> {
            this.smtpServer.reset();
            x.success();
        }).flatMap((p) -> Mono.empty());
    }

    @Override
    public Mono<Message> getById(String messageId) {
        return Flux.fromIterable(this.smtpServer.getReceivedEmails())
                .filter((x) -> x.getHeaderValue("Message-ID").equalsIgnoreCase(messageId))
                .reduce((a, b) -> b)
                .map(this::transform);
    }

    @Override
    public Mono<Void> deleteById(String messageId) {
        return Flux.fromIterable(this.smtpServer.getReceivedEmails())
                .filter((x) -> x.getHeaderValue("Message-ID").equalsIgnoreCase(messageId))
                .reduce((a, b) -> b)
                .doOnNext((x) -> {
                  this.smtpServer.getReceivedEmails().remove(x);
                })
                .then();
    }

    /**
     *
     * @param smtpMessage
     * @return
     */
    private Message transform(SmtpMessage smtpMessage) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Message message = new Message();
        message.setBody(smtpMessage.getBody());
        message.setSubject(smtpMessage.getHeaderValue("Subject"));
        message.setFrom(smtpMessage.getHeaderValue("From"));
        try {
            message.setSent(format.parse(smtpMessage.getHeaderValue("Date")));
        } catch (ParseException e) {
            log.error("", e);
        }
        message.setTo(new String[]{smtpMessage.getHeaderValue("To")});
        message.setCc(new String[]{smtpMessage.getHeaderValue("Cc")});
        message.setMessageId(smtpMessage.getHeaderValue("Message-ID"));
        message.setContentType(smtpMessage.getHeaderValue("Content-Type"));
        return message;
    }
}
