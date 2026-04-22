package com.winecellar.importer.infrastructure.mongo.cellar;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CellarRepository extends MongoRepository<CellarDocument, String> {

  Optional<CellarDocument> findFirstByOrderByCreatedAtAsc();

  Optional<CellarDocument> findByName(String name);
}
