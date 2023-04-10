package com.ermanag.sensor1microservice.controller;

import com.ermanag.sensor1microservice.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sensorController")
public class SensorController {

    @Autowired
    SensorService sensorService;

    @PostMapping(value = "/sensorLocation")
    public void sendMessageToKafkaTopicLocation(@RequestParam("latitude") String latitude, @RequestParam("latitude") String longitude) {
        sensorService.sendLocationMessage(latitude, longitude);
    }

    @PostMapping(value = "/sensorKerteriz")
    public void sendMessageToKafkaTopicKerteriz(@RequestParam("latitude") String sensorLatitude, @RequestParam("latitude") String sensorLongitude, @RequestParam("latitude") String hedefLatitude, @RequestParam("latitude") String hedefLongitude) {
        sensorService.sendKerterizMessage(sensorLatitude, sensorLongitude, hedefLatitude, hedefLongitude);
    }

}
