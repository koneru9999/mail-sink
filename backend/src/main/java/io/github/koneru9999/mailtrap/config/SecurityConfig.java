package io.github.koneru9999.mailtrap.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/mails", "/mails/**", "/reset", "/**/*.js", "/**/*.css", "/**/*.html").permitAll()
                .anyRequest().authenticated();
    }
}
