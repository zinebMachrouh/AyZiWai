package org.example.ayziwai.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ayziwai.entities.enums.AlertSeverity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "alerts")
public class Alert {
    @Id
    private String id;

    private String message;

    private AlertSeverity severity;

    private LocalDateTime timestamp;

    @DocumentReference
    private Device device;
}
