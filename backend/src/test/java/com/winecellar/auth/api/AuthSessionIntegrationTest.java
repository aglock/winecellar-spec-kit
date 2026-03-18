package com.winecellar.auth.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.winecellar.auth.application.ActivationService;
import com.winecellar.auth.application.RegistrationService;
import com.winecellar.auth.application.SessionService;
import com.winecellar.auth.domain.AccountStatus;
import com.winecellar.auth.domain.UserAccount;
import com.winecellar.cellar.api.CellarController;
import com.winecellar.cellar.application.CellarQueryService;
import com.winecellar.config.AuthProperties;
import com.winecellar.shared.api.ApiExceptionHandler;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AuthSessionIntegrationTest {

    private SessionService sessionService;
    private CellarQueryService cellarQueryService;
    private MockMvc authMvc;
    private MockMvc cellarMvc;

    @BeforeEach
    void setUp() {
        RegistrationService registrationService = Mockito.mock(RegistrationService.class);
        ActivationService activationService = Mockito.mock(ActivationService.class);
        sessionService = Mockito.mock(SessionService.class);
        cellarQueryService = Mockito.mock(CellarQueryService.class);

        authMvc = MockMvcBuilders.standaloneSetup(
                        new AuthController(
                                registrationService,
                                activationService,
                                sessionService,
                                new AuthProperties("WC_SESSION", 12, 15)))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();

        cellarMvc = MockMvcBuilders.standaloneSetup(new CellarController(cellarQueryService))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void signInSetsSessionCookie() throws Exception {
        UserAccount account = new UserAccount("user-1", "collector@example.com", "collector@example.com", "hash", "Collector", AccountStatus.ACTIVE, Instant.now(), Instant.now());
        when(sessionService.signIn(any(), any()))
                .thenReturn(new SessionService.SignInResult("session-token", account, Instant.now().plusSeconds(3600)));

        authMvc.perform(post("/api/auth/sign-in")
                        .contentType("application/json")
                        .content("""
                                {"email":"collector@example.com","password":"very-secure-password"}
                                """))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("WC_SESSION"));
    }

    @Test
    void sessionEndpointReturnsCurrentSession() throws Exception {
        when(sessionService.requireCurrentSession("session-token"))
                .thenReturn(new SessionService.AuthenticatedSessionView("session-token", "user-1", "collector@example.com", "Collector", Instant.parse("2026-03-17T12:00:00Z")));

        authMvc.perform(get("/api/auth/session").cookie(new jakarta.servlet.http.Cookie("WC_SESSION", "session-token")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id").value("user-1"));
    }

    @Test
    void cellarsEndpointReturnsMembershipScopedItems() throws Exception {
        when(cellarQueryService.listForUser("user-1"))
                .thenReturn(List.of(new CellarQueryService.CellarSummaryView("cellar-1", "Reserve", "Bordeaux", "Private cellar", "OWNER", null, null)));

        MockHttpServletRequestBuilder request = get("/api/cellars")
                .principal(new UsernamePasswordAuthenticationToken("user-1", "session-token"));

        cellarMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].cellarId").value("cellar-1"));
    }
}
