package io.github.koneru9999.mailtrap.service;

import io.github.koneru9999.mailtrap.controller.Message;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MailsFetchService {

    Flux<Message> fetchEmails(Pageable page);

    Mono<Void> clearMessages();

    Mono<Message> getById(String messageId);

    Mono<Void> deleteById(String messageId);
}
