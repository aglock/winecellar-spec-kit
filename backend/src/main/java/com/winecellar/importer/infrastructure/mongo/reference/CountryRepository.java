package com.winecellar.importer.infrastructure.mongo.reference;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CountryRepository extends MongoRepository<CountryDocument, String> {

  Optional<CountryDocument> findByNormalizedName(String normalizedName);
}
