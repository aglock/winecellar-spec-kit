package com.winecellar.auth.domain;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("account_activation_tokens")
public record AccountActivationToken(
        @Id String id,
        String userAccountId,
        String tokenHash,
        Instant issuedAt,
        Instant expiresAt,
        Instant consumedAt,
        String deliveryStatus) {

    public AccountActivationToken consume(Instant consumedAt) {
        return new AccountActivationToken(id, userAccountId, tokenHash, issuedAt, expiresAt, consumedAt, deliveryStatus);
    }
}
