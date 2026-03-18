package com.winecellar.auth.delivery;

import com.winecellar.config.FrontendProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingActivationDeliveryAdapter implements ActivationDeliveryPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingActivationDeliveryAdapter.class);

    private final FrontendProperties frontendProperties;

    public LoggingActivationDeliveryAdapter(FrontendProperties frontendProperties) {
        this.frontendProperties = frontendProperties;
    }

    @Override
    public DeliveryResult deliver(String email, String activationToken) {
        String activationLink = "%s/activate?token=%s".formatted(frontendProperties.baseUrl(), activationToken);
        LOGGER.info("Activation link for {}: {}", email, activationLink);
        return new DeliveryResult(activationLink, "LOGGED");
    }
}
