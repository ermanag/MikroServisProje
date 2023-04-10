package com.ermanag.merkezibirimmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class MerkezibirimmicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MerkezibirimmicroserviceApplication.class, args);
    }

}
