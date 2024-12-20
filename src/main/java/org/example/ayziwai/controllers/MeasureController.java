package org.example.ayziwai.controllers;

import java.nio.charset.StandardCharsets;

import org.example.ayziwai.dto.request.MeasureRequest;
import org.example.ayziwai.dto.response.MeasureResponse;
import org.example.ayziwai.services.interfaces.MeasureService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/measures")
@RequiredArgsConstructor
public class MeasureController {
    private final MeasureService measureService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<MeasureResponse>> getAllMeasures(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(measureService.getAllMeasures(PageRequest.of(page, size)));
    }

    @GetMapping("/device/{deviceId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<MeasureResponse>> getMeasuresByDevice(
            @PathVariable String deviceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(measureService.getMeasuresByDevice(deviceId, PageRequest.of(page, size)));
    }

    @GetMapping("/export")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Resource> exportMeasuresAsCSV() {
        String csvContent = measureService.exportMeasures();
        ByteArrayResource resource = new ByteArrayResource(csvContent.getBytes(StandardCharsets.UTF_8));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=measures.csv")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<MeasureResponse> saveMeasure(@Valid @RequestBody MeasureRequest measureRequest) {
        return ResponseEntity.ok(measureService.saveMeasure(measureRequest));
    }
}
