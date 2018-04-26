package io.github.koneru9999.mailtrap.controller;

import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private String subject;
    private String body;
    private String from;
    private String[] to;
    private Date sent;
    private String[] cc;
    private String messageId;
    private String mimeVersion;
    private String contentType;
}
