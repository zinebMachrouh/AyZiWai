package org.example.ayziwai.services.implementation;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.ayziwai.entities.Alert;
import org.example.ayziwai.entities.Device;
import org.example.ayziwai.entities.enums.AlertSeverity;
import org.example.ayziwai.repositories.AlertRepository;
import org.example.ayziwai.repositories.DeviceRepository;
import org.example.ayziwai.services.interfaces.AlertService;
import org.example.ayziwai.utils.AlertEvaluator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AlertServiceImpl implements AlertService {
    private final AlertRepository alertRepository;
    private final DeviceRepository deviceRepository;

    @Override
    public Page<Alert> getAlerts(Pageable pageable) {
        return alertRepository.findAll(pageable);
    }

    @Override
    public Page<Alert> getAlertsByDeviceId(String deviceId, Pageable pageable) {
        return alertRepository.findByDeviceId(deviceId, pageable);
    }

    @Override
    public List<Alert> getAlertsBySeverity(String severity) {
        try {
            AlertSeverity alertSeverity = AlertSeverity.valueOf(severity.toUpperCase());
            return alertRepository.findBySeverity(alertSeverity);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid severity level provided.");
        }
    }

    @Override
    public Alert createAlert(String deviceId, double measurementValue) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));

        Pair<AlertSeverity, String> result = AlertEvaluator.evaluateAlert(device.getType().toString(), measurementValue);

        Alert alert = Alert.builder()
                .device(device)
                .severity(result.getFirst())
                .message(result.getSecond())
                .timestamp(LocalDateTime.now())
                .build();

        return alertRepository.save(alert);
    }

}
