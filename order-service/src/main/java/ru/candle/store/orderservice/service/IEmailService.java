package ru.candle.store.orderservice.service;

import ru.candle.store.orderservice.entity.EmailDetails;

public interface IEmailService {

    void sendEmail(EmailDetails details);
}
