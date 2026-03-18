package com.winecellar.auth.delivery;

import static org.assertj.core.api.Assertions.assertThat;

import com.winecellar.config.FrontendProperties;
import org.junit.jupiter.api.Test;

class LoggingActivationDeliveryAdapterTest {

    @Test
    void buildsActivationLinkFromFrontendBaseUrl() {
        LoggingActivationDeliveryAdapter adapter = new LoggingActivationDeliveryAdapter(new FrontendProperties("http://localhost:5173"));

        ActivationDeliveryPort.DeliveryResult result = adapter.deliver("collector@example.com", "token-123");

        assertThat(result.activationLink()).isEqualTo("http://localhost:5173/activate?token=token-123");
        assertThat(result.status()).isEqualTo("LOGGED");
    }
}
