package ru.candle.store.emailservice.service;

import ru.candle.store.emailservice.entity.EmailDetails;

public interface EmailService {

    String sendSimpleMail(EmailDetails details);
    String sendMailWithAttachment(EmailDetails details);

}
