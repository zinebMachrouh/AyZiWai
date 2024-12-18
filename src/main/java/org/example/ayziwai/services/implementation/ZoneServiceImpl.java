package org.example.ayziwai.services.implementation;

import lombok.RequiredArgsConstructor;
import org.example.ayziwai.dto.request.ZoneRequest;
import org.example.ayziwai.dto.response.ZoneResponse;
import org.example.ayziwai.entities.Zone;
import org.example.ayziwai.exceptions.DoesNotExistsException;
import org.example.ayziwai.mapper.ZoneMapper;
import org.example.ayziwai.repositories.ZoneRepository;
import org.example.ayziwai.services.interfaces.ZoneService;
 import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;
    private final ZoneMapper zoneMapper;

    @Override
    public ZoneResponse getZoneById(String id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new DoesNotExistsException("Zone not found with id: " + id));
        return zoneMapper.toResponse(zone);
    }

    @Override
    public ZoneResponse createZone(ZoneRequest zoneRequest) {
        Zone zone = zoneMapper.toEntity(zoneRequest);
        Zone savedZone = zoneRepository.save(zone);
        return zoneMapper.toResponse(savedZone);
    }
}
