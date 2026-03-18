package com.winecellar.auth.domain;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("user_accounts")
public record UserAccount(
        @Id String id,
        String normalizedEmail,
        String email,
        String passwordHash,
        String displayName,
        AccountStatus status,
        Instant createdAt,
        Instant activatedAt) {

    public UserAccount activate(Instant activatedAt) {
        return new UserAccount(id, normalizedEmail, email, passwordHash, displayName, AccountStatus.ACTIVE, createdAt, activatedAt);
    }
}
