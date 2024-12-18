package org.example.ayziwai.services.interfaces;

import org.example.ayziwai.dto.request.ZoneRequest;
import org.example.ayziwai.dto.response.ZoneResponse;

public interface ZoneService {
    ZoneResponse getZoneById(String id);
    ZoneResponse createZone(ZoneRequest zoneRequest);
}
