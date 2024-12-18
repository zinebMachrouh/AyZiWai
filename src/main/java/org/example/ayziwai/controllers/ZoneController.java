package org.example.ayziwai.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ayziwai.dto.request.ZoneRequest;
import org.example.ayziwai.dto.response.ZoneResponse;
import org.example.ayziwai.services.interfaces.ZoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @GetMapping("/api/user/zones/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ZoneResponse> getZone(@PathVariable String id) {
        return ResponseEntity.ok(zoneService.getZoneById(id));
    }

    @PostMapping("/api/admin/zones")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ZoneResponse> createZone(@Valid @RequestBody ZoneRequest zoneRequest) {
        return new ResponseEntity<>(zoneService.createZone(zoneRequest), HttpStatus.CREATED);
    }
}
