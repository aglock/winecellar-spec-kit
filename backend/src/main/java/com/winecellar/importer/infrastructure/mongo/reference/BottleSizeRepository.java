package com.winecellar.importer.infrastructure.mongo.reference;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BottleSizeRepository extends MongoRepository<BottleSizeDocument, String> {

  Optional<BottleSizeDocument> findByNormalizedLabel(String normalizedLabel);
}
