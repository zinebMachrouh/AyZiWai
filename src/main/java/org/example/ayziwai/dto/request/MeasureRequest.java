package org.example.ayziwai.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeasureRequest {
    @NotNull(message = "Value is required")
    @DecimalMin(value = "0.0", message = "Value must be greater than 0")
    private Double value;

    @NotBlank(message = "Device ID is required")
    private String deviceId;

}
