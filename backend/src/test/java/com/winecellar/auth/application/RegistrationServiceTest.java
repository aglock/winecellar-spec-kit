package com.winecellar.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.winecellar.auth.audit.IdentityAccessEventService;
import com.winecellar.auth.delivery.ActivationDeliveryPort;
import com.winecellar.auth.domain.UserAccount;
import com.winecellar.auth.repo.AccountActivationTokenRepository;
import com.winecellar.auth.repo.UserAccountRepository;
import com.winecellar.config.AuthProperties;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class RegistrationServiceTest {

    private UserAccountRepository userAccountRepository;
    private AccountActivationTokenRepository tokenRepository;
    private RegistrationService service;

    @BeforeEach
    void setUp() {
        userAccountRepository = Mockito.mock(UserAccountRepository.class);
        tokenRepository = Mockito.mock(AccountActivationTokenRepository.class);
        ActivationDeliveryPort deliveryPort = Mockito.mock(ActivationDeliveryPort.class);
        when(deliveryPort.deliver(any(), any()))
                .thenReturn(new ActivationDeliveryPort.DeliveryResult("http://localhost:5173/activate?token=token", "LOGGED"));
        when(userAccountRepository.save(any()))
                .thenAnswer(invocation -> {
                    UserAccount account = invocation.getArgument(0);
                    return new UserAccount("user-1", account.normalizedEmail(), account.email(), account.passwordHash(), account.displayName(), account.status(), account.createdAt(), account.activatedAt());
                });
        when(tokenRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        service = new RegistrationService(
                userAccountRepository,
                tokenRepository,
                new BCryptPasswordEncoder(),
                new TokenFactory(),
                new AuthProperties("WC_SESSION", 12, 15),
                deliveryPort,
                Mockito.mock(IdentityAccessEventService.class));
    }

    @Test
    void registersPendingAccountAndReturnsActivationExpiry() {
        when(userAccountRepository.findByNormalizedEmail("collector@example.com")).thenReturn(Optional.empty());

        RegistrationService.RegistrationResult result =
                service.register("collector@example.com", "very-secure-password", "Collector");

        assertThat(result.userAccountId()).isEqualTo("user-1");
        assertThat(result.activationExpiresAt()).isAfter(Instant.now());
    }

    @Test
    void rejectsDuplicateEmailRegistration() {
        when(userAccountRepository.findByNormalizedEmail("collector@example.com"))
                .thenReturn(Optional.of(new UserAccount("user-1", "collector@example.com", "collector@example.com", "hash", "Collector", null, Instant.now(), null)));

        assertThatThrownBy(() -> service.register("collector@example.com", "very-secure-password", "Collector"))
                .isInstanceOf(AuthException.class)
                .extracting("status")
                .isEqualTo(HttpStatus.CONFLICT);
    }
}
