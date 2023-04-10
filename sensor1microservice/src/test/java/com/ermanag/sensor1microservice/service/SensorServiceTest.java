package com.ermanag.sensor1microservice.service;

import com.ermanag.sensor1microservice.kafka.utils.TestUtils;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class SensorServiceTest {

    private KafkaProducer<String, String> producer;
    private String topicNameLocation;
    private String topicNameKerteriz;

    @BeforeEach
    void setUp() {

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(properties);
        topicNameLocation = "test-location-data";
        topicNameKerteriz = "test-kerteriz-data";
    }

    @AfterEach
    void tearDown() {
        producer.close();
    }

    @Test
    void sendLocationMessage() throws ExecutionException, InterruptedException {
        String value = "{\"latitude\": 5, longitude\":1}";
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicNameLocation, value);
        producer.send(producerRecord).get();


        KafkaConsumer<String, String> consumer = TestUtils.createConsumer("localhost:9092", "test_location_group", "latest");
        consumer.subscribe(Collections.singletonList(topicNameLocation));
        ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
        consumerRecords.forEach(record -> {
            assertEquals(value, record.value());
        });
        consumer.commitSync();
        consumer.close();
    }

    @Test
    void sendKerterizMessage() throws ExecutionException, InterruptedException {
        String kerterizAciklama ="Hedefin kerterizi Y pozitif ekseninde açýsý 45 derece";
        String value = "{\"kerterizDegree\": 45, \"kerterizAciklama\":" +kerterizAciklama+"}";
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicNameKerteriz, value);
        producer.send(producerRecord).get();


        KafkaConsumer<String, String> consumer = TestUtils.createConsumer("localhost:9092", "test_kerteriz_group", "latest");
        consumer.subscribe(Collections.singletonList(topicNameKerteriz));
        ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));
        consumerRecords.forEach(record -> {
            assertEquals(value, record.value());
        });
        consumer.commitSync();
        consumer.close();
    }
}