package com.winecellar.auth.domain;

import java.time.Instant;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("authenticated_sessions")
public record AuthenticatedSession(
        @Id String id,
        String sessionToken,
        String userAccountId,
        Instant issuedAt,
        Instant expiresAt,
        Instant endedAt,
        String endReason,
        Map<String, String> clientMetadata) {

    public boolean isActiveAt(Instant now) {
        return endedAt == null && expiresAt.isAfter(now);
    }

    public AuthenticatedSession end(Instant endedAt, String endReason) {
        return new AuthenticatedSession(id, sessionToken, userAccountId, issuedAt, expiresAt, endedAt, endReason, clientMetadata);
    }
}
