package org.example.ayziwai.repositories;

import org.example.ayziwai.entities.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {
    
    Page<Device> findByZone(String zone, Pageable pageable);
}
