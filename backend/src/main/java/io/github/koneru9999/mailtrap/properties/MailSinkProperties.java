package io.github.koneru9999.mailtrap.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Venkaiah Chowdary Koneru
 */
@Component
@ConfigurationProperties(prefix = "mailsink")
@Getter
@Setter
public class MailSinkProperties {
    private int dummyDataSize;
}
