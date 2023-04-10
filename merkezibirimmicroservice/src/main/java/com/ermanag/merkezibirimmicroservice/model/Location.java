package com.ermanag.merkezibirimmicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "locations")
public class Location {

    @Id
    private String sensorID;
    private String sensorX;
    private String sensorY;
}
