package com.winecellar.cellar.domain;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("cellars")
public record Cellar(
        @Id String id,
        String name,
        String location,
        String description,
        Instant createdAt) {
}
