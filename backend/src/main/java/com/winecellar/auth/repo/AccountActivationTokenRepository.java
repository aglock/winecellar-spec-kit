package com.winecellar.auth.repo;

import com.winecellar.auth.domain.AccountActivationToken;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountActivationTokenRepository extends MongoRepository<AccountActivationToken, String> {
    Optional<AccountActivationToken> findByTokenHash(String tokenHash);
}
