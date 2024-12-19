package org.example.ayziwai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeasureResponse {
    private String id;

    private LocalDateTime timestamp;

    private Double value;

    private String deviceId;
}
