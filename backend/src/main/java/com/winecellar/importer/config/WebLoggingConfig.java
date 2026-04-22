package com.winecellar.importer.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebLoggingConfig implements WebMvcConfigurer {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebLoggingConfig.class);
  private static final String START_NANOS_ATTRIBUTE = "winecellar.request.startNanos";

  @Override
  public void addInterceptors(@NonNull InterceptorRegistry registry) {
    registry.addInterceptor(new ApiRequestLoggingInterceptor())
        .addPathPatterns("/api/**");
  }

  private static final class ApiRequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Object handler
    ) {
      request.setAttribute(START_NANOS_ATTRIBUTE, System.nanoTime());
      return true;
    }

    @Override
    public void afterCompletion(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull Object handler,
      @Nullable Exception ex
    ) {
      Object startAttribute = request.getAttribute(START_NANOS_ATTRIBUTE);
      long durationMs = startAttribute instanceof Long startNanos
          ? (System.nanoTime() - startNanos) / 1_000_000
          : -1;

      String method = request.getMethod();
      String path = request.getRequestURI();
      int status = response.getStatus();

      if ("POST".equals(method) && "/api/imports/wine-bottles".equals(path)) {
        LOGGER.info("import.request method={} path={} status={} durationMs={}", method, path, status, durationMs);
      } else {
        LOGGER.debug("api.request method={} path={} status={} durationMs={}", method, path, status, durationMs);
      }
    }
  }
}
