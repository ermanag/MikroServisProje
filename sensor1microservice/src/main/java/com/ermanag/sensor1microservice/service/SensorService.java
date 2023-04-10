package com.ermanag.sensor1microservice.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SensorService {

    private static final String TOPIC_LOCATION = "location-data";
    private static final String TOPIC_KERTERIZ = "kerteriz-data";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendLocationMessage(String latitude,  String longitude) {
        String locationData = "{\"latitude\":" + latitude + ", \"longitude\":" +longitude+"}";
        this.kafkaTemplate.send(TOPIC_LOCATION, locationData);
    }

    public void sendKerterizMessage(String sensorLatitude, String sensorLongitude, String hedefLatitude, String hedefLongitude) {
        double deltaX = Double.parseDouble(hedefLatitude) - Double.parseDouble(sensorLatitude);
        double deltaY = Double.parseDouble(sensorLongitude) - Double.parseDouble(hedefLongitude);
        double kerteriz = Math.atan2(deltaY, deltaX);
        kerteriz = Math.toDegrees(kerteriz);

        if (kerteriz < 0) {
            kerteriz += 360;
        }

        double kerterizDegree = 90 - kerteriz;

        String kerterizAciklama ="Hedefin kerterizi Y pozitif ekseninde açısı " + kerterizDegree + " derece";
        String kerterizData = "{\"kerterizDegree\":" + kerterizDegree + ", \"kerterizAciklama\":" +kerterizAciklama+"}";
        this.kafkaTemplate.send(TOPIC_KERTERIZ, kerterizData);
    }
}
