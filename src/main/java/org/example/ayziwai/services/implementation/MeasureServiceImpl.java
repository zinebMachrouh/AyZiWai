package org.example.ayziwai.services.implementation;

import lombok.AllArgsConstructor;
import org.example.ayziwai.entities.Device;
import org.example.ayziwai.entities.Measure;
import org.example.ayziwai.repositories.DeviceRepository;
import org.example.ayziwai.repositories.MeasureRepository;
import org.example.ayziwai.services.interfaces.AlertService;
import org.example.ayziwai.services.interfaces.MeasureService;
import org.springframework.stereotype.Service;
import org.example.ayziwai.dto.request.MeasureRequest;
import org.example.ayziwai.dto.response.MeasureResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MeasureServiceImpl implements MeasureService {
    private final MeasureRepository measureRepository;
    private final DeviceRepository deviceRepository;

    private final AlertService alertService;

    @Override
    public MeasureResponse saveMeasure(MeasureRequest measureRequest) {
        Device device = deviceRepository.findById(measureRequest.getDeviceId())
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));

        Measure measure = Measure.builder()
                .value(measureRequest.getValue())
                .timestamp(LocalDateTime.now())
                .device(device)
                .build();

        Measure savedMeasure = measureRepository.save(measure);

        alertService.createAlert(savedMeasure.getDevice().getId(), savedMeasure.getValue());
        return mapToResponse(savedMeasure);
    }

    @Override
    public Page<MeasureResponse> getAllMeasures(Pageable pageable) {
        return measureRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<MeasureResponse> getMeasuresByDevice(String deviceId, Pageable pageable) {
        return measureRepository.findByDevice_Id(deviceId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public String exportMeasures() {
        List<Measure> measures = measureRepository.findAll();

        StringBuilder csvBuilder = new StringBuilder("ID,Timestamp,Value,DeviceID\n");

        String csvContent = measures.stream()
                .map(measure -> String.format("%s,%s,%.2f,%s",
                        measure.getId(),
                        measure.getTimestamp(),
                        measure.getValue(),
                        measure.getDevice().getId()))
                .collect(Collectors.joining("\n"));

        csvBuilder.append(csvContent);
        return csvBuilder.toString();
    }

    private MeasureResponse mapToResponse(Measure measure) {
        return MeasureResponse.builder()
                .id(measure.getId())
                .timestamp(measure.getTimestamp())
                .value(measure.getValue())
                .deviceId(measure.getDevice().getId())
                .build();
    }
}
