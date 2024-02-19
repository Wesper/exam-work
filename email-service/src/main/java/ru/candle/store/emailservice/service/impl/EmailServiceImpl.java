package ru.candle.store.emailservice.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.candle.store.emailservice.entity.EmailDetails;
import ru.candle.store.emailservice.service.EmailService;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendSimpleMail(EmailDetails details) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(details.getRecipient());
            message.setText(details.getMsgBody());
            message.setSubject(details.getSubject());

            mailSender.send(message);
            return "Письмо успешно отправлено";
        } catch (Exception e) {
            return "Ошибка при отправке письма";
        }
    }

    @Override
    public String sendMailWithAttachment(EmailDetails details) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(details.getSubject());

            FileSystemResource file = new FileSystemResource(details.getAttachment());
            mimeMessageHelper.addAttachment(file.getFilename(), file);
            mailSender.send(message);
            return "Письмо успешно отправлено";
        } catch (MessagingException e) {
            return "Ошибка при отправке письма";
        }
    }
}
