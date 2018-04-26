package io.github.koneru9999.mailtrap.config;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;

@Configuration
public class SMTPConfiguration implements ApplicationRunner {

    @Bean
    public SimpleSmtpServer getSmtpServer() throws IOException {
        return SimpleSmtpServer.start(1025);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i =0; i < 10; i++) {
            sendMessage(getSmtpServer().getPort(), "sender@here.com", "Test" + (i+1), "Test Body", "receiver@there.com");
        }
    }

    private Properties getMailProperties(int port) {
        Properties mailProps = new Properties();
        mailProps.setProperty("mail.smtp.host", "localhost");
        mailProps.setProperty("mail.smtp.port", "" + port);
        mailProps.setProperty("mail.smtp.sendpartial", "true");
        return mailProps;
    }

    private void sendMessage(int port, String from, String subject, String body, String to) throws MessagingException {
        Properties mailProps = getMailProperties(port);
        Session session = Session.getInstance(mailProps, null);
        //session.setDebug(true);

        MimeMessage msg = createMessage(session, from, to, subject, body);
        Transport.send(msg);
    }

    private MimeMessage createMessage(
            Session session, String from, String to, String subject, String body) throws MessagingException {
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setText(body);
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        return msg;
    }
}
