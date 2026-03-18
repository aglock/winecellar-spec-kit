package com.winecellar.auth.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.winecellar.auth.application.ActivationService;
import com.winecellar.auth.application.RegistrationService;
import com.winecellar.auth.application.SessionService;
import com.winecellar.config.AuthProperties;
import com.winecellar.shared.api.ApiExceptionHandler;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AuthRegistrationIntegrationTest {

    private RegistrationService registrationService;
    private ActivationService activationService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        registrationService = Mockito.mock(RegistrationService.class);
        activationService = Mockito.mock(ActivationService.class);
        SessionService sessionService = Mockito.mock(SessionService.class);

        mockMvc = MockMvcBuilders.standaloneSetup(
                        new AuthController(
                                registrationService,
                                activationService,
                                sessionService,
                                new AuthProperties("WC_SESSION", 12, 15)))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void registerReturnsPendingActivationResponse() throws Exception {
        when(registrationService.register(any(), any(), any()))
                .thenReturn(new RegistrationService.RegistrationResult("user-1", Instant.parse("2026-03-17T12:00:00Z")));

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content("""
                                {"email":"collector@example.com","password":"very-secure-password","displayName":"Collector"}
                                """))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("PENDING_ACTIVATION"));
    }

    @Test
    void activateReturnsActiveStatus() throws Exception {
        mockMvc.perform(post("/api/auth/activate")
                        .contentType("application/json")
                        .content("""
                                {"token":"token-value-1234567890"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
}
