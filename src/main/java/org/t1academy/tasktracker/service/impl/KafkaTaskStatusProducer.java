package org.t1academy.tasktracker.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.t1academy.tasktracker.dto.TaskNotificationDto;
import org.t1academy.tasktracker.service.TaskStatusProducer;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaTaskStatusProducer implements TaskStatusProducer {

    @Value("${app.kafka.producer.topic}")
    private String topic;

    private final KafkaTemplate<Long, TaskNotificationDto> kafkaTemplate;


    @Override
    public void sendTaskStatusUpdate(TaskNotificationDto taskNotificationDto) {
        try {
            kafkaTemplate.send(topic, taskNotificationDto.getId(), taskNotificationDto)
                    .whenComplete((result, exception) -> {
                        if (exception != null) {
                            log.error("Failed to send message. Topic = {}, Message = {}",
                                    topic, taskNotificationDto, exception);
                        } else {
                            log.info("Message sent successfully: {}", result.getRecordMetadata());
                        }
                    });
        } catch (Exception e) {
            log.error("Failed to send message.", e);
        }
    }
}
