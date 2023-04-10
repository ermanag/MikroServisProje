package com.ermanag.merkezibirimmicroservice.service;

import com.ermanag.merkezibirimmicroservice.model.KerterizData;
import com.ermanag.merkezibirimmicroservice.model.Location;
import com.ermanag.merkezibirimmicroservice.repository.KerterizDataRepository;
import com.ermanag.merkezibirimmicroservice.repository.LocationRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    KerterizDataRepository kerterizDataRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public KafkaConsumerService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @KafkaListener(topics = "location-data", groupId = "location-group")
    public void consumeLocation(String message) {
        JSONObject jsonObj = new JSONObject(message);
        double latitude = jsonObj.getDouble("latitude");
        double longitude = jsonObj.getDouble("longitude");
        Location location = new Location();
        location.setSensorX(Double.toString(latitude));
        location.setSensorY(Double.toString(longitude));
        mongoTemplate.save(location);
    }

    @KafkaListener(topics = "kerteriz-data", groupId = "kerteriz-group")
    public void consumeKerteriz(String message) {
        JSONObject jsonObj = new JSONObject(message);
        int degree = jsonObj.getInt("kerterizDegree");
        KerterizData kerterizData = new KerterizData();
        kerterizData.setKerterizDegree(Integer.toString(degree));
        mongoTemplate.save(kerterizData);
    }

    public void hedefKonumBul(String sensorId1, String sensorId2){
        Location sensor1 = locationRepository.findLocationById(sensorId1);
        Location sensor2 = locationRepository.findLocationById(sensorId2);
        double sensor1x = Double.valueOf(sensor1.getSensorX());
        double sensor1y = Double.valueOf(sensor1.getSensorY());
        double sensor2x = Double.valueOf(sensor2.getSensorX());
        double sensor2y = Double.valueOf(sensor2.getSensorY());

        KerterizData kerterizData = kerterizDataRepository.findKerterizDataById(sensorId1);

        double angle1 = Math.toRadians(Double.valueOf(kerterizData.getKerterizDegree()));

        double distance = Math.sqrt(Math.pow(sensor2x - sensor1x, 2) + Math.pow(sensor2y - sensor1y, 2));
        double hedefx = sensor1x + distance * Math.cos(angle1);
        double hedefy = sensor1y + distance * Math.sin(angle1);

        DBObject dbObject = BasicDBObject.parse(hedefx + " & " + hedefy);
        mongoTemplate.save(dbObject, "hedef-konum-collection");
    }
}
