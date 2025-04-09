package org.t1academy.tasktracker.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.t1academy.tasktracker.dto.TaskNotificationDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaNotificationConsumer {
    private final NotificationService notificationService;

    @KafkaListener(containerFactory = "kafkaListenerContainerFactory",
            topics = "${app.kafka.consumer.topic}",
            groupId = "${app.kafka.consumer.group-id}")
    public void listenTaskStatusUpdate(List<ConsumerRecord<Long, TaskNotificationDto>> records, Acknowledgment ack) {
        for (var record : records) {
            try {
                log.debug("Processing message {} with key {} in topic {}", record.value(), record.key(), record.topic());
                notificationService.notifyTaskStatusUpdate(record.value());
            } catch (Exception e) {
                log.error("Failed to process message {} with key {} in topic {}", record.value(), record.key(), record.topic(), e);
            }
        }

        ack.acknowledge();
        log.info("Received messages have been processed. topic {}, batchSize {}", records.get(0).topic(), records.size());
    }
}
