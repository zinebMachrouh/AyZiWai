package org.example.ayziwai.repositories;

import org.example.ayziwai.entities.Zone;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepository extends MongoRepository<Zone, String> {
    // La méthode findById est déjà héritée de MongoRepository
    // Pas besoin de la redéclarer
}
