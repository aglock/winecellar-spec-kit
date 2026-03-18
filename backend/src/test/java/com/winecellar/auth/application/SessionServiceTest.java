package com.winecellar.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.winecellar.auth.audit.IdentityAccessEventService;
import com.winecellar.auth.domain.AccountStatus;
import com.winecellar.auth.domain.AuthenticatedSession;
import com.winecellar.auth.domain.UserAccount;
import com.winecellar.auth.repo.AuthenticatedSessionRepository;
import com.winecellar.auth.repo.UserAccountRepository;
import com.winecellar.config.AuthProperties;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class SessionServiceTest {

    private UserAccountRepository userAccountRepository;
    private AuthenticatedSessionRepository sessionRepository;
    private SessionService service;

    @BeforeEach
    void setUp() {
        userAccountRepository = Mockito.mock(UserAccountRepository.class);
        sessionRepository = Mockito.mock(AuthenticatedSessionRepository.class);
        when(sessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        service = new SessionService(
                userAccountRepository,
                sessionRepository,
                new BCryptPasswordEncoder(),
                new TokenFactory(),
                new AuthProperties("WC_SESSION", 12, 15),
                Mockito.mock(IdentityAccessEventService.class));
    }

    @Test
    void rejectsInvalidCredentials() {
        UserAccount account = new UserAccount("user-1", "collector@example.com", "collector@example.com", new BCryptPasswordEncoder().encode("correct-password"), "Collector", AccountStatus.ACTIVE, Instant.now(), Instant.now());
        when(userAccountRepository.findByNormalizedEmail("collector@example.com")).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> service.signIn("collector@example.com", "wrong-password"))
                .isInstanceOf(AuthException.class)
                .extracting("status")
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void authenticatesActiveSession() {
        Instant expiresAt = Instant.now().plus(12, ChronoUnit.HOURS);
        when(sessionRepository.findBySessionToken("session-token"))
                .thenReturn(Optional.of(new AuthenticatedSession("session-1", "session-token", "user-1", Instant.now(), expiresAt, null, null, java.util.Map.of())));
        when(userAccountRepository.findById("user-1"))
                .thenReturn(Optional.of(new UserAccount("user-1", "collector@example.com", "collector@example.com", "hash", "Collector", AccountStatus.ACTIVE, Instant.now(), Instant.now())));

        assertThat(service.authenticate("session-token")).isPresent();
    }
}
