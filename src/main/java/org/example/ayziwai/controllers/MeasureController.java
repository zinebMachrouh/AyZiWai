package org.example.ayziwai.controllers;

import lombok.AllArgsConstructor;
import org.example.ayziwai.services.interfaces.MeasureService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.example.ayziwai.dto.request.MeasureRequest;
import org.example.ayziwai.dto.response.MeasureResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/measures")
@AllArgsConstructor
public class MeasureController {
    private final MeasureService measureService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<MeasureResponse> saveMeasure(@Valid @RequestBody MeasureRequest measureRequest) {
        MeasureResponse savedMeasure = measureService.saveMeasure(measureRequest);
        return ResponseEntity.ok(savedMeasure);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<MeasureResponse>> getAllMeasures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<MeasureResponse> measures = measureService.getAllMeasures(PageRequest.of(page, size));
        return ResponseEntity.ok(measures);
    }

    @GetMapping("/device/{deviceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<MeasureResponse>> getMeasuresByDevice(
            @PathVariable String deviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<MeasureResponse> measures = measureService.getMeasuresByDevice(deviceId, PageRequest.of(page, size));
        return ResponseEntity.ok(measures);
    }

    @GetMapping("/export")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Resource> exportMeasuresAsCSV() {
        String csvContent = measureService.exportMeasures();
        ByteArrayResource resource = new ByteArrayResource(csvContent.getBytes(StandardCharsets.UTF_8));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=measures.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }
}
