package org.t1academy.tasktracker.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.t1academy.tasktracker.dto.TaskNotificationDto;
import org.t1academy.tasktracker.entity.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailNotificationServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailNotificationService emailNotificationService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailNotificationService, "fromAddress", "from@example.com");
        ReflectionTestUtils.setField(emailNotificationService, "toAddress", "to@example.com");
    }

    @Test
    void notifyTaskStatusUpdate_shouldSendEmail() {
        TaskNotificationDto taskNotificationDto = new TaskNotificationDto(1L, TaskStatus.IN_PROGRESS);

        emailNotificationService.notifyTaskStatusUpdate(taskNotificationDto);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));

    }
}