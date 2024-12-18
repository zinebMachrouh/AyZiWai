package org.example.ayziwai.services.implementation;

import lombok.RequiredArgsConstructor;
import org.example.ayziwai.dto.request.DeviceRequest;
import org.example.ayziwai.dto.response.DeviceResponse;
import org.example.ayziwai.entities.Device;
import org.example.ayziwai.mapper.DeviceMapper;
import org.example.ayziwai.repositories.DeviceRepository;
import org.example.ayziwai.services.interfaces.DeviceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
    
    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    @Override
    public Page<DeviceResponse> getAllDevices(Pageable pageable) {
        return deviceRepository.findAll(pageable)
                .map(deviceMapper::toResponse);
    }

     
    @Override
    public Page<DeviceResponse> getDevicesByZone(String zone, Pageable pageable) {
        return deviceRepository.findByZone(zone, pageable)
                .map(deviceMapper::toResponse);
    }
    

    @Override
    public DeviceResponse createDevice(DeviceRequest deviceRequest) {
        Device device = deviceMapper.toEntity(deviceRequest);
        Device savedDevice = deviceRepository.save(device);
        return deviceMapper.toResponse(savedDevice);
    }
}
