package com.winecellar.auth.application;

import com.winecellar.auth.audit.IdentityAccessEventService;
import com.winecellar.auth.delivery.ActivationDeliveryPort;
import com.winecellar.auth.domain.AccountActivationToken;
import com.winecellar.auth.domain.AccountStatus;
import com.winecellar.auth.domain.IdentityEventType;
import com.winecellar.auth.domain.UserAccount;
import com.winecellar.auth.repo.AccountActivationTokenRepository;
import com.winecellar.auth.repo.UserAccountRepository;
import com.winecellar.config.AuthProperties;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UserAccountRepository userAccountRepository;
    private final AccountActivationTokenRepository activationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenFactory tokenFactory;
    private final AuthProperties authProperties;
    private final ActivationDeliveryPort activationDeliveryPort;
    private final IdentityAccessEventService identityAccessEventService;

    public RegistrationService(
            UserAccountRepository userAccountRepository,
            AccountActivationTokenRepository activationTokenRepository,
            PasswordEncoder passwordEncoder,
            TokenFactory tokenFactory,
            AuthProperties authProperties,
            ActivationDeliveryPort activationDeliveryPort,
            IdentityAccessEventService identityAccessEventService) {
        this.userAccountRepository = userAccountRepository;
        this.activationTokenRepository = activationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenFactory = tokenFactory;
        this.authProperties = authProperties;
        this.activationDeliveryPort = activationDeliveryPort;
        this.identityAccessEventService = identityAccessEventService;
    }

    public RegistrationResult register(String email, String password, String displayName) {
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        if (userAccountRepository.findByNormalizedEmail(normalizedEmail).isPresent()) {
            throw new AuthException("EMAIL_ALREADY_EXISTS", "An account with that email already exists", HttpStatus.CONFLICT);
        }

        Instant now = Instant.now();
        UserAccount account = userAccountRepository.save(new UserAccount(
                null,
                normalizedEmail,
                email.trim(),
                passwordEncoder.encode(password),
                displayName.trim(),
                AccountStatus.PENDING_ACTIVATION,
                now,
                null));

        identityAccessEventService.append(null, account.id(), IdentityEventType.ACCOUNT_REGISTERED, Map.of("email", normalizedEmail));

        String rawToken = tokenFactory.newToken();
        Instant expiresAt = now.plus(authProperties.activationLinkTtl());
        ActivationDeliveryPort.DeliveryResult deliveryResult = activationDeliveryPort.deliver(account.email(), rawToken);
        activationTokenRepository.save(new AccountActivationToken(
                null,
                account.id(),
                tokenFactory.hash(rawToken),
                now,
                expiresAt,
                null,
                deliveryResult.status()));

        identityAccessEventService.append(
                null,
                account.id(),
                IdentityEventType.ACTIVATION_ISSUED,
                Map.of("expiresAt", expiresAt.toString(), "deliveryStatus", deliveryResult.status()));

        return new RegistrationResult(account.id(), expiresAt);
    }

    public record RegistrationResult(String userAccountId, Instant activationExpiresAt) {
    }
}
