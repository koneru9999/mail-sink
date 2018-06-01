package io.github.koneru9999.mailsink.config;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;
import io.github.koneru9999.mailsink.properties.MailSinkProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.MILLIS;

@Configuration
@Slf4j
public class SMTPConfiguration implements ApplicationRunner {

    @Autowired
    private MailSinkProperties mailSinkProperties;

    @Bean(destroyMethod = "stop")
    public GreenMail greenMail() {
        GreenMail greenMail = new GreenMail(new ServerSetup(3025, "0.0.0.0",
                ServerSetup.PROTOCOL_SMTP));
        greenMail.start();

        return greenMail;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (mailSinkProperties.getDummyDataSize() > 0) {
            String mailBody = "<html><body><h1>Test body<h1><br>Hello user. <span>abcdefgh</span></body></html>";
            Flux.range(0, mailSinkProperties.getDummyDataSize())
                    .map((x) -> {
                        GreenMailUtil.sendMessageBody("receiver@there.com,receiver2@there.com", "sender@here.com",
                                "Test" + (x + 1), mailBody, "text/html", ServerSetupTest.SMTP);
                        return x;
                    })
                    .delayElements(Duration.of(200, MILLIS))
                    .doOnComplete(() -> log.info("Initial Data completed"))
                    .subscribe();
        }
    }
}
