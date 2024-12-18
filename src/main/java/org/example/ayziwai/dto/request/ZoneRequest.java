package org.example.ayziwai.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class ZoneRequest {
    @NotBlank(message = "Zone name is required")
    @Size(min = 2, max = 100, message = "Zone name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Zone type is required")
    private String type;

    @NotBlank(message = "Zone location is required")
    private String location;
}
