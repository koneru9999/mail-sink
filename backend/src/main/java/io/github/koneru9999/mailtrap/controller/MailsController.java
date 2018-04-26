package io.github.koneru9999.mailtrap.controller;

import io.github.koneru9999.mailtrap.service.MailsFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class MailsController {
    private final MailsFetchService mailsFetchService;

    @Autowired
    public MailsController(MailsFetchService mailsFetchService) {
        this.mailsFetchService = mailsFetchService;
    }

    @GetMapping("/mails")
    public Flux<Message> retrieveAllMails(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                          @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return mailsFetchService.fetchEmails(PageRequest.of(page > 0 ? page - 1 : 0, size));
    }

    @DeleteMapping("/reset")
    public Mono<Void> clearMessages() {
        return mailsFetchService.clearMessages();
    }

    @GetMapping(value = "/mails/{id}")
    public Mono<Message> getMail(@PathVariable(name = "id") String messageId) {
        return mailsFetchService.getById(messageId);
    }

    @DeleteMapping(value = "/mails/{id}")
    public Mono<Void> deleteMail(@PathVariable(name = "id") String messageId) {
        return mailsFetchService.deleteById(messageId);
    }
}
