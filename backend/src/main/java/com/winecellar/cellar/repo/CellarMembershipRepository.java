package com.winecellar.cellar.repo;

import com.winecellar.cellar.domain.CellarMembership;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CellarMembershipRepository extends MongoRepository<CellarMembership, String> {
    List<CellarMembership> findByUserId(String userId);
}
