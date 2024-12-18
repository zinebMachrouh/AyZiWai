package org.example.ayziwai.mapper;

import org.example.ayziwai.dto.request.DeviceRequest;
import org.example.ayziwai.dto.response.DeviceResponse;
import org.example.ayziwai.entities.Device;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    @Mapping(target = "id", ignore = true)
    Device toEntity(DeviceRequest request);

    DeviceResponse toResponse(Device device);
}
