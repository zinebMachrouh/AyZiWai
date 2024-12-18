package org.example.ayziwai.entities;

import java.time.LocalDateTime;

import org.example.ayziwai.entities.enums.DeviceStatus;
import org.example.ayziwai.entities.enums.DeviceType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "devices")
@Data
public class Device {
    @Id
    private String id;
    private String name;
    private DeviceType type;
    private DeviceStatus status;
    private LocalDateTime lastCheck;
    private String zone;
    
}
