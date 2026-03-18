package com.winecellar.auth.repo;

import com.winecellar.auth.domain.UserAccount;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {
    Optional<UserAccount> findByNormalizedEmail(String normalizedEmail);
}
