package com.winecellar.importer.infrastructure.mongo.reference;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RegionRepository extends MongoRepository<RegionDocument, String> {

  Optional<RegionDocument> findByCountryIdAndNormalizedName(String countryId, String normalizedName);
}
