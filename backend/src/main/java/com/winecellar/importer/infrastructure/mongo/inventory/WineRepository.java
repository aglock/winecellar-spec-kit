package com.winecellar.importer.infrastructure.mongo.inventory;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface WineRepository extends MongoRepository<WineDocument, String> {
}
