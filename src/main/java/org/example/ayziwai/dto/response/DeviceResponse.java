package org.example.ayziwai.dto.response;

import java.time.LocalDateTime;

import org.example.ayziwai.entities.enums.DeviceStatus;
import org.example.ayziwai.entities.enums.DeviceType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceResponse {
    private String name;
    private DeviceType type;
    private DeviceStatus status;
    private LocalDateTime lastCheck;
}
