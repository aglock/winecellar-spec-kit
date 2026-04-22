package com.winecellar.importer.infrastructure.mongo.inventory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BottleRepository extends MongoRepository<BottleDocument, String> {

	Page<BottleDocument> findByCellarId(String cellarId, Pageable pageable);
}
