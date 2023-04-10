package com.ermanag.merkezibirimmicroservice.controller;

import com.ermanag.merkezibirimmicroservice.service.KafkaConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/hedefKonumController")
public class HedefKonumController {

    @Autowired
    KafkaConsumerService kafkaConsumerService;

    @GetMapping(value = "/hedefLocation")
    public void hedefLocation(@RequestParam("sensor1Id") String sensor1Id, @RequestParam("sensor2Id") String sensor2Id) {
        kafkaConsumerService.hedefKonumBul(sensor1Id, sensor2Id);
    }
}
