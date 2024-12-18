package org.example.ayziwai.mapper;

import org.example.ayziwai.dto.request.ZoneRequest;
import org.example.ayziwai.dto.response.ZoneResponse;
import org.example.ayziwai.entities.Zone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ZoneMapper {

    @Mapping(target = "id", ignore = true)
    Zone toEntity(ZoneRequest request);

    ZoneResponse toResponse(Zone zone);
}
