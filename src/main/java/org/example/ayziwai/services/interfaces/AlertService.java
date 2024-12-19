package org.example.ayziwai.services.interfaces;

import org.example.ayziwai.entities.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AlertService {
    Page<Alert> getAlerts(Pageable pageable);

    Page<Alert> getAlertsByDeviceId(String deviceId, Pageable pageable);

    List<Alert> getAlertsBySeverity(String severity);

    Alert createAlert(String deviceId, double measurementValue);
}
