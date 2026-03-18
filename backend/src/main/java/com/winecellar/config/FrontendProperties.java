package com.winecellar.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "winecellar.frontend")
public record FrontendProperties(String baseUrl) {
}
