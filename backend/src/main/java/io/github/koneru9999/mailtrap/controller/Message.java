package io.github.koneru9999.mailtrap.controller;

import lombok.Data;

import javax.mail.Address;
import java.util.Date;

@Data
public class Message {
    private String subject;
    private String body;
    private Address from;
    private Address[] to;
    private Date sent;
    private Address[] cc;
    private String messageId;
    private String contentId;
    private String mimeVersion;
    private String contentType;
}
