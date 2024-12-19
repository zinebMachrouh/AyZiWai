package org.example.ayziwai.services.interfaces;

import org.example.ayziwai.dto.request.MeasureRequest;
import org.example.ayziwai.dto.response.MeasureResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MeasureService {
    MeasureResponse saveMeasure(MeasureRequest measureRequest);

    Page<MeasureResponse> getAllMeasures(Pageable pageable);

    Page<MeasureResponse> getMeasuresByDevice(String deviceId, Pageable pageable);

    String exportMeasures();
}
