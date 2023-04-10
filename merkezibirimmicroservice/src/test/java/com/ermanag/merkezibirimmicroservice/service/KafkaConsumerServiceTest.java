package com.ermanag.merkezibirimmicroservice.service;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

class KafkaConsumerServiceTest {

    private static final String TEST_TOPIC = "test-location-data";
    private static final String TEST_TOPIC2 = "test-kerteriz-data";
    private static final String TEST_MESSAGE = "{\"latitude\": -, longitude\":1}";
    private static final String TEST_MESSAGE4 = "{\"latitude\": 5, longitude\":-1}";
    private static final String TEST_MESSAGE2 = "{\"kerterizDegree\": 45, \"kerterizAciklama\":Hedefin kerterizi Y pozitif ekseninde açýsý 45 derece}";
    private static final String TEST_MESSAGE3 = "{\"kerterizDegree\": 315, \"kerterizAciklama\":Hedefin kerterizi Y pozitif ekseninde açýsý 315 derece}";
    private Producer<String, String> producer;
    private Consumer<String, String> consumer;

    @BeforeEach
    void setUp() {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(producerProps);


        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList(TEST_TOPIC));
        consumer.subscribe(Collections.singletonList(TEST_TOPIC2));
    }

    @AfterEach
    void tearDown() {
        producer.close();
        consumer.close();
    }

    @Test
    void consumeLocation() {
        ProducerRecord<String, String> record = new ProducerRecord<>(TEST_TOPIC, TEST_MESSAGE);
        producer.send(record);
        record = new ProducerRecord<>(TEST_TOPIC, TEST_MESSAGE4);
        producer.send(record);

        ConsumerRecords<String, String> records = consumer.poll(10000);
        assertEquals(1, records.count());
        ConsumerRecord<String, String> consumedRecord = records.iterator().next();
        assertEquals(TEST_MESSAGE, consumedRecord.value());
        assertEquals(TEST_MESSAGE4, consumedRecord.value());
    }

    @Test
    void consumeKerteriz() {
        ProducerRecord<String, String> record = new ProducerRecord<>(TEST_TOPIC2, TEST_MESSAGE2);
        producer.send(record);
        record = new ProducerRecord<>(TEST_TOPIC2, TEST_MESSAGE3);
        producer.send(record);

        ConsumerRecords<String, String> records = consumer.poll(10000);
        assertEquals(1, records.count());
        ConsumerRecord<String, String> consumedRecord = records.iterator().next();
        assertEquals(TEST_MESSAGE2, consumedRecord.value());
        assertEquals(TEST_MESSAGE3, consumedRecord.value());
    }
}