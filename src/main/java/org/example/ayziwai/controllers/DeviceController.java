package org.example.ayziwai.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ayziwai.dto.request.DeviceRequest;
import org.example.ayziwai.dto.response.DeviceResponse;
import org.example.ayziwai.services.interfaces.DeviceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    // @GetMapping("/api/user/devices")
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    // public ResponseEntity<Page<DeviceResponse>> getAllDevices(Pageable pageable) {
    //     return ResponseEntity.ok(deviceService.getAllDevices(pageable));
    // }

    @PostMapping("/api/admin/devices")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeviceResponse> createDevice(@RequestBody DeviceRequest deviceRequest) {
        return new ResponseEntity<>(deviceService.createDevice(deviceRequest), HttpStatus.CREATED);
    }
}
