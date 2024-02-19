package ru.candle.store.orderservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.candle.store.orderservice.entity.EmailDetails;
import ru.candle.store.orderservice.service.IEmailService;

@Service
@Slf4j
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private KafkaTemplate<String, EmailDetails> kafkaTemplate;

    @Value("${email.topic.name}")
    private String topicName;

    public void sendEmail(EmailDetails details) {
        log.info("Начали отправку сообщения");
        kafkaTemplate.send(topicName, details);
        log.info("Сообщение успешно отправлено");
    }
}
