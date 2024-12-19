package org.example.ayziwai.repositories;

import org.example.ayziwai.entities.Alert;
import org.example.ayziwai.entities.enums.AlertSeverity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AlertRepository extends MongoRepository<Alert, String> {
    Page<Alert> findByDeviceId(String deviceId, Pageable pageable);
    List<Alert> findBySeverity(AlertSeverity severity);
}
