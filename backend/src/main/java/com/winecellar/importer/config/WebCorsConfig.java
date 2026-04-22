package com.winecellar.importer.config;

import java.util.Objects;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCorsConfig implements WebMvcConfigurer {

  private final WebCorsProperties webCorsProperties;

  public WebCorsConfig(WebCorsProperties webCorsProperties) {
    this.webCorsProperties = webCorsProperties;
  }

  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    String[] allowedOrigins = Objects.requireNonNull(
        webCorsProperties.getAllowedOrigins().toArray(String[]::new)
    );
    registry.addMapping("/api/**")
        .allowedOrigins(allowedOrigins)
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .exposedHeaders("Location")
        .maxAge(3600);
  }
}
