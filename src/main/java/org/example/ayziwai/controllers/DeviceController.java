package org.example.ayziwai.controllers;

import org.example.ayziwai.dto.request.DeviceRequest;
import org.example.ayziwai.dto.response.DeviceResponse;
import org.example.ayziwai.services.interfaces.DeviceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping("/api/user/devices")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getAllDevices(Pageable pageable) {
        try {
            Page<DeviceResponse> devices = deviceService.getAllDevices(pageable);
            return ResponseEntity.ok(devices);
        } catch (AccessDeniedException e) {
            return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Access denied: Insufficient privileges");
        }
    }

    @PostMapping("/api/admin/devices")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createDevice(@RequestBody DeviceRequest deviceRequest) {
        try {
            DeviceResponse device = deviceService.createDevice(deviceRequest);
            return new ResponseEntity<>(device, HttpStatus.CREATED);
        } catch (AccessDeniedException e) {
            return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Access denied: Admin privileges required");
        }
    }
}
