package com.winecellar.auth.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.winecellar.auth.audit.IdentityAccessEventService;
import com.winecellar.auth.domain.AccountActivationToken;
import com.winecellar.auth.domain.AccountStatus;
import com.winecellar.auth.domain.UserAccount;
import com.winecellar.auth.repo.AccountActivationTokenRepository;
import com.winecellar.auth.repo.UserAccountRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ActivationServiceTest {

    private AccountActivationTokenRepository tokenRepository;
    private UserAccountRepository userAccountRepository;
    private ActivationService service;
    private TokenFactory tokenFactory;

    @BeforeEach
    void setUp() {
        tokenRepository = Mockito.mock(AccountActivationTokenRepository.class);
        userAccountRepository = Mockito.mock(UserAccountRepository.class);
        tokenFactory = new TokenFactory();
        when(tokenRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(userAccountRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        service = new ActivationService(
                tokenRepository,
                userAccountRepository,
                tokenFactory,
                Mockito.mock(IdentityAccessEventService.class));
    }

    @Test
    void rejectsExpiredTokens() {
        when(tokenRepository.findByTokenHash(tokenFactory.hash("token")))
                .thenReturn(Optional.of(new AccountActivationToken("token-1", "user-1", tokenFactory.hash("token"), Instant.now().minus(20, ChronoUnit.MINUTES), Instant.now().minus(5, ChronoUnit.MINUTES), null, "LOGGED")));

        assertThatThrownBy(() -> service.activate("token")).isInstanceOf(AuthException.class);
    }

    @Test
    void activatesAccountWhenTokenIsValid() {
        when(tokenRepository.findByTokenHash(tokenFactory.hash("token")))
                .thenReturn(Optional.of(new AccountActivationToken("token-1", "user-1", tokenFactory.hash("token"), Instant.now(), Instant.now().plus(15, ChronoUnit.MINUTES), null, "LOGGED")));
        when(userAccountRepository.findById("user-1"))
                .thenReturn(Optional.of(new UserAccount("user-1", "collector@example.com", "collector@example.com", "hash", "Collector", AccountStatus.PENDING_ACTIVATION, Instant.now(), null)));

        service.activate("token");
    }
}
