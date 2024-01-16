package com.sourav.blindauction.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class KafkaEmailService {

    @Value("${kafka.bootstrap.servers}")
    private String kafkaBootstrapServers;

    @Value("${kafka.email.topic}")
    private String emailTopic;

    public void sendEmailNotification(String to, String subject, String body) throws ExecutionException, InterruptedException {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaBootstrapServers);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        try (Producer<String, String> producer = new KafkaProducer<>(properties)) {
            String emailData = String.format("%s,%s,%s", to, subject, body);
            producer.send(new ProducerRecord<>(emailTopic, emailData)).get();
        } catch (Exception e) {
            log.error("Exception occurred while sending email data to kafka topic ",e);
        }
    }
}