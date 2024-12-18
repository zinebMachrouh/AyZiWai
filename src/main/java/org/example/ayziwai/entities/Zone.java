package org.example.ayziwai.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "zones")
@Data
public class Zone {

    @Id
    private String id;
    private String name;
    private String type;
    private String location;
}
