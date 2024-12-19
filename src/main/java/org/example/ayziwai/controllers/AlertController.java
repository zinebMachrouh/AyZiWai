package org.example.ayziwai.controllers;

import lombok.AllArgsConstructor;
import org.example.ayziwai.entities.Alert;
import org.example.ayziwai.services.interfaces.AlertService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@AllArgsConstructor
public class AlertController {
    private final AlertService alertService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<Alert>> getAllAlerts(Pageable pageable) {
        Page<Alert> alerts = alertService.getAlerts(pageable);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/device/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<Alert>> getAlertsByDeviceId(@PathVariable String deviceId, Pageable pageable) {
        Page<Alert> alerts = alertService.getAlertsByDeviceId(deviceId, pageable);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/severity/{severity}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Alert>> getAlertsBySeverity(@PathVariable String severity) {
        List<Alert> alerts = alertService.getAlertsBySeverity(severity);
        return ResponseEntity.ok(alerts);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Alert> createAlert(@RequestParam String deviceId, @RequestParam double measurementValue) {
        Alert alert = alertService.createAlert(deviceId, measurementValue);
        return ResponseEntity.ok(alert);
    }
}
