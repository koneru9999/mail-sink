package io.github.koneru9999.mailsink.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "io.github.koneru9999")
@EnableAutoConfiguration
public class AppConfig {
}
