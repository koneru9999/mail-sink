package io.github.koneru9999.mailtrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MailTrapApplication {

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(MailTrapApplication.class);
//    }
    public static void main(String[] args) {
        SpringApplication.run(MailTrapApplication.class, args);
    }
}
