package org.example.ayziwai.dto.request;

import java.time.LocalDateTime;

import org.example.ayziwai.entities.enums.DeviceType;
import org.example.ayziwai.entities.enums.DeviceStatus;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class DeviceRequest {
    @NotBlank(message = "Device name is required")
    @Size(min = 2, max = 100, message = "Device name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Device type is required")
    private DeviceType type;

    @NotNull(message = "Device status is required")
    private DeviceStatus status;

    @NotNull(message = "Last check is required")
    private LocalDateTime lastCheck;
}
