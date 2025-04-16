package org.t1academy.tasktracker.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.t1academy.tasktracker.dto.TaskNotificationDto;
import org.t1academy.tasktracker.dto.serialization.TaskNotificationDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaConsumerConfig {

    @Value("${app.kafka.bootstrap-servers}")
    private  String bootstrapServers;
    @Value("${app.kafka.consumer.group-id}")
    private  String groupId;
    @Value("${app.kafka.consumer.session-timeout}")
    private  String sessionTimeout;
    @Value("${app.kafka.consumer.max-partition-fetch-bytes}")
    private  String maxPartitionFetchBytes;
    @Value("${app.kafka.consumer.max-poll-records}")
    private  String maxPollRecords;
    @Value("${app.kafka.consumer.max-poll-interval}")
    private  String maxPollInterval;
    @Value("${app.kafka.consumer.auto-offset-reset}")
    private  String autoOffsetReset;

    @Bean
    public ConsumerFactory<Long, TaskNotificationDto> consumerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, TaskNotificationDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "org.t1academy.tasktracker.dto.TaskNotificationDto");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollInterval);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    private CommonErrorHandler errorHandler() {
        ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries(3);
        backOff.setInitialInterval(1000);
        DefaultErrorHandler handler = new DefaultErrorHandler(backOff);
        handler.addNotRetryableExceptions(IllegalStateException.class);
        handler.setRetryListeners((record, ex, deliveryAttempt) ->
                log.error("RetryListeners message = {}, offset = {}, deliveryAttempt = {}", ex.getMessage(), record.offset(), deliveryAttempt));
        return handler;
    }

    private <T> void factoryBuilder(ConsumerFactory<Long, T> consumerFactory,
                                    ConcurrentKafkaListenerContainerFactory<Long, T> factory) {
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(1);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setPollTimeout(5000);
        factory.getContainerProperties().setMicrometerEnabled(true);
        factory.setCommonErrorHandler(errorHandler());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, TaskNotificationDto> kafkaListenerContainerFactory(
            ConsumerFactory<Long, TaskNotificationDto> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Long, TaskNotificationDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factoryBuilder(consumerFactory, factory);

        return factory;
    }
}
