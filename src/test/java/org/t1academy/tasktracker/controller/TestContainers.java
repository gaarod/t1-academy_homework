package org.t1academy.tasktracker.controller;


import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class TestContainers {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16.3-alpine");

    @Container
    private static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.9.0"));

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");

        registry.add("app.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("app.kafka.consumer.topic", () -> "task-notification-topic");
        registry.add("app.kafka.consumer.group-id", () -> "task-consumer-group");
        registry.add("app.kafka.consumer.session-timeout", () -> "15000");
        registry.add("app.kafka.consumer.max-partition-fetch-bytes", () -> "300000");
        registry.add("app.kafka.consumer.max-poll-records", () -> "1");
        registry.add("app.kafka.consumer.max-poll-interval", () -> "100000");
        registry.add("app.kafka.consumer.auto-offset-reset", () -> "earliest");
        registry.add("app.kafka.producer.topic", () -> "task-notification-topic");
        registry.add("app.kafka.producer.acknowledgement", () -> "1");
    }

}
