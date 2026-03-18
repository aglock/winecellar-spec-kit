package com.winecellar.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "winecellar.auth")
public record AuthProperties(
        String sessionCookieName,
        int sessionDurationHours,
        int activationLinkTtlMinutes) {

    public Duration sessionDuration() {
        return Duration.ofHours(sessionDurationHours);
    }

    public Duration activationLinkTtl() {
        return Duration.ofMinutes(activationLinkTtlMinutes);
    }
}
