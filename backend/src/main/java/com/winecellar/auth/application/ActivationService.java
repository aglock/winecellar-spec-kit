package com.winecellar.auth.application;

import com.winecellar.auth.audit.IdentityAccessEventService;
import com.winecellar.auth.domain.AccountActivationToken;
import com.winecellar.auth.domain.IdentityEventType;
import com.winecellar.auth.domain.UserAccount;
import com.winecellar.auth.repo.AccountActivationTokenRepository;
import com.winecellar.auth.repo.UserAccountRepository;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ActivationService {

    private final AccountActivationTokenRepository activationTokenRepository;
    private final UserAccountRepository userAccountRepository;
    private final TokenFactory tokenFactory;
    private final IdentityAccessEventService identityAccessEventService;

    public ActivationService(
            AccountActivationTokenRepository activationTokenRepository,
            UserAccountRepository userAccountRepository,
            TokenFactory tokenFactory,
            IdentityAccessEventService identityAccessEventService) {
        this.activationTokenRepository = activationTokenRepository;
        this.userAccountRepository = userAccountRepository;
        this.tokenFactory = tokenFactory;
        this.identityAccessEventService = identityAccessEventService;
    }

    public void activate(String rawToken) {
        AccountActivationToken token = activationTokenRepository.findByTokenHash(tokenFactory.hash(rawToken))
                .orElseThrow(() -> activationRejected(null, "INVALID_TOKEN"));

        if (token.consumedAt() != null) {
            throw activationRejected(token.userAccountId(), "TOKEN_ALREADY_USED");
        }
        if (token.expiresAt().isBefore(Instant.now())) {
            throw activationRejected(token.userAccountId(), "TOKEN_EXPIRED");
        }

        UserAccount account = userAccountRepository.findById(token.userAccountId())
                .orElseThrow(() -> new AuthException("ACCOUNT_NOT_FOUND", "Account was not found", HttpStatus.NOT_FOUND));
        userAccountRepository.save(account.activate(Instant.now()));
        activationTokenRepository.save(token.consume(Instant.now()));
        identityAccessEventService.append(account.id(), account.id(), IdentityEventType.ACTIVATION_SUCCEEDED, Map.of("tokenId", token.id()));
    }

    private AuthException activationRejected(String subjectUserId, String reason) {
        if (subjectUserId != null) {
            identityAccessEventService.append(subjectUserId, subjectUserId, IdentityEventType.ACTIVATION_REJECTED, Map.of("reason", reason));
        }
        return new AuthException("ACTIVATION_FAILED", "Activation attempt did not succeed", HttpStatus.BAD_REQUEST);
    }
}
