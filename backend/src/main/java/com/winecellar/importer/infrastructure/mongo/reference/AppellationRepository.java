package com.winecellar.importer.infrastructure.mongo.reference;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppellationRepository extends MongoRepository<AppellationDocument, String> {

  Optional<AppellationDocument> findByRegionIdAndNormalizedName(String regionId, String normalizedName);
}
