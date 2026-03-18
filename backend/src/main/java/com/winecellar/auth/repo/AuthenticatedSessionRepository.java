package com.winecellar.auth.repo;

import com.winecellar.auth.domain.AuthenticatedSession;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthenticatedSessionRepository extends MongoRepository<AuthenticatedSession, String> {
    Optional<AuthenticatedSession> findBySessionToken(String sessionToken);
}
