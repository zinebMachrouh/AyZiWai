package org.example.ayziwai.services.interfaces;

import org.example.ayziwai.dto.request.DeviceRequest;
import org.example.ayziwai.dto.response.DeviceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeviceService {
    Page<DeviceResponse> getAllDevices(Pageable pageable);
    Page<DeviceResponse> getDevicesByZone(String zone, Pageable pageable);
    DeviceResponse createDevice(DeviceRequest deviceRequest);
}
