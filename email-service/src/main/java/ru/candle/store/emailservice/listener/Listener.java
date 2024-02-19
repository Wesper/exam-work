package ru.candle.store.emailservice.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.candle.store.emailservice.entity.EmailDetails;
import ru.candle.store.emailservice.service.EmailService;

import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
public class Listener {

    @Autowired
    private EmailService emailService;

    private CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = "${email.topic.name}", groupId = "${mail.group.id}", containerFactory="kafkaListenerContainerFactory")
    public void registerListener(EmailDetails details) {
        log.info("Получено сообщение " + details);
        emailService.sendSimpleMail(details);
        log.info("Сообщение успешно отправлено");
        latch.countDown();
    }
}
