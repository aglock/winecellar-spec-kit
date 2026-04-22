package com.winecellar.importer.infrastructure.mongo.reference;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProducerRepository extends MongoRepository<ProducerDocument, String> {

  Optional<ProducerDocument> findByNormalizedName(String normalizedName);
}
