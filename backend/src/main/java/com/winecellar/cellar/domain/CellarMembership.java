package com.winecellar.cellar.domain;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("cellar_memberships")
public record CellarMembership(
        @Id String id,
        String userId,
        String cellarId,
        MembershipRole role,
        Instant grantedAt,
        String grantedByUserId) {
}
