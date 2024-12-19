package org.example.ayziwai.repositories;

import io.micrometer.core.instrument.Measurement;
import org.example.ayziwai.entities.Measure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MeasureRepository extends MongoRepository<Measure, String> {
    Page<Measure> findByDevice_Id(String deviceId, Pageable pageable);
}
