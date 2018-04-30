package io.github.koneru9999.mailtrap.config;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
@Slf4j
public class SMTPConfiguration implements ApplicationRunner {

    @Bean(destroyMethod = "stop")
    public GreenMail greenMail() {
        GreenMail greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();

        return greenMail;
    }

    @Override
    public void run(ApplicationArguments args) {
        String mailBody = "<html><body><h1>Test body<h1><br>Hello user. <span>abcdefgh</span></body></html>";
        Flux.range(0, 57)
                .map((x) -> {
                    GreenMailUtil.sendMessageBody("receiver@there.com", "sender@here.com",
                            "Test" + (x + 1), mailBody, "text/html", ServerSetupTest.SMTP);
                    return x;
                })
                .doOnComplete(() -> log.info("Initial Data completed"))
                .subscribe();
    }
}
