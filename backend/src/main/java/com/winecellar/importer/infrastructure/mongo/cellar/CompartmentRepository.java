package com.winecellar.importer.infrastructure.mongo.cellar;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompartmentRepository extends MongoRepository<CompartmentDocument, String> {

  Optional<CompartmentDocument> findFirstByCellarIdOrderByCreatedAtAsc(String cellarId);
}
