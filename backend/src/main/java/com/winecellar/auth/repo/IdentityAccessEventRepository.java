package com.winecellar.auth.repo;

import com.winecellar.auth.domain.IdentityAccessEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IdentityAccessEventRepository extends MongoRepository<IdentityAccessEvent, String> {
}
