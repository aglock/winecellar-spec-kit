package com.winecellar.cellar.repo;

import com.winecellar.cellar.domain.Cellar;
import java.util.Collection;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CellarRepository extends MongoRepository<Cellar, String> {
    List<Cellar> findByIdIn(Collection<String> ids);
}
