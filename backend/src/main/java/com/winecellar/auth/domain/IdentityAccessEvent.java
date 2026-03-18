package com.winecellar.auth.domain;

import java.time.Instant;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("identity_access_events")
public record IdentityAccessEvent(
        @Id String id,
        String actorUserId,
        String subjectUserAccountId,
        IdentityEventType eventType,
        Instant occurredAt,
        String primaryTargetType,
        String primaryTargetId,
        Map<String, String> metadata) {
}
