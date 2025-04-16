package org.t1academy.tasktracker.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.t1academy.tasktracker.aspect.annotation.LogException;
import org.t1academy.tasktracker.dto.TaskNotificationDto;
import org.t1academy.tasktracker.service.NotificationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    @Value("${app.notification.mail.fromAddress}")
    private String fromAddress;
    @Value("${app.notification.mail.toAddress}")
    private String toAddress;

    private final JavaMailSender emailSender;

    @Override
    @LogException
    public void notifyTaskStatusUpdate(TaskNotificationDto taskNotification) {
        String subject = String.format("Status of task with id %d updated", taskNotification.getId());
        String text = String.format("Status of task with id %d has been updated to %s", taskNotification.getId(), taskNotification.getStatus());
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromAddress);
        mailMessage.setTo(toAddress);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);

        try {
            emailSender.send(mailMessage);
            log.debug("Email sent to {}", toAddress);
        } catch (MailException e) {
            log.warn("Failed to send email to {}: {}", toAddress, e.getMessage());
        }
    }


}
