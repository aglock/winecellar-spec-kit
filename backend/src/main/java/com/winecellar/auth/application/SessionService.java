package com.winecellar.auth.application;

import com.winecellar.auth.audit.IdentityAccessEventService;
import com.winecellar.auth.domain.AccountStatus;
import com.winecellar.auth.domain.AuthenticatedSession;
import com.winecellar.auth.domain.IdentityEventType;
import com.winecellar.auth.domain.UserAccount;
import com.winecellar.auth.repo.AuthenticatedSessionRepository;
import com.winecellar.auth.repo.UserAccountRepository;
import com.winecellar.config.AuthProperties;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    private final UserAccountRepository userAccountRepository;
    private final AuthenticatedSessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenFactory tokenFactory;
    private final AuthProperties authProperties;
    private final IdentityAccessEventService identityAccessEventService;

    public SessionService(
            UserAccountRepository userAccountRepository,
            AuthenticatedSessionRepository sessionRepository,
            PasswordEncoder passwordEncoder,
            TokenFactory tokenFactory,
            AuthProperties authProperties,
            IdentityAccessEventService identityAccessEventService) {
        this.userAccountRepository = userAccountRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenFactory = tokenFactory;
        this.authProperties = authProperties;
        this.identityAccessEventService = identityAccessEventService;
    }

    public SignInResult signIn(String email, String password) {
        String normalizedEmail = email.trim().toLowerCase();
        UserAccount account = userAccountRepository.findByNormalizedEmail(normalizedEmail)
                .orElseThrow(() -> signInFailed(null, normalizedEmail, "INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED));

        if (account.status() != AccountStatus.ACTIVE) {
            throw signInFailed(account.id(), normalizedEmail, "ACCOUNT_NOT_ACTIVE", HttpStatus.FORBIDDEN);
        }
        if (!passwordEncoder.matches(password, account.passwordHash())) {
            throw signInFailed(account.id(), normalizedEmail, "INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED);
        }

        Instant now = Instant.now();
        AuthenticatedSession session = sessionRepository.save(new AuthenticatedSession(
                null,
                tokenFactory.newToken(),
                account.id(),
                now,
                now.plus(authProperties.sessionDuration()),
                null,
                null,
                Map.of()));
        identityAccessEventService.append(account.id(), account.id(), IdentityEventType.SIGN_IN_SUCCEEDED, Map.of("expiresAt", session.expiresAt().toString()));
        return new SignInResult(session.sessionToken(), account, session.expiresAt());
    }

    public void signOut(String sessionToken) {
        AuthenticatedSession session = sessionRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new AuthException("SESSION_NOT_FOUND", "No active session found", HttpStatus.UNAUTHORIZED));
        if (session.endedAt() == null) {
            sessionRepository.save(session.end(Instant.now(), "SIGN_OUT"));
            identityAccessEventService.append(session.userAccountId(), session.userAccountId(), IdentityEventType.SESSION_ENDED, Map.of("reason", "SIGN_OUT"));
        }
    }

    public Optional<AuthenticatedSessionView> authenticate(String sessionToken) {
        return sessionRepository.findBySessionToken(sessionToken)
                .flatMap(this::toAuthenticatedSessionView);
    }

    public AuthenticatedSessionView requireCurrentSession(String sessionToken) {
        return authenticate(sessionToken)
                .orElseThrow(() -> new AuthException("SESSION_INVALID", "Session missing or expired", HttpStatus.UNAUTHORIZED));
    }

    private Optional<AuthenticatedSessionView> toAuthenticatedSessionView(AuthenticatedSession session) {
        Instant now = Instant.now();
        if (session.endedAt() != null) {
            return Optional.empty();
        }
        if (!session.isActiveAt(now)) {
            sessionRepository.save(session.end(now, "EXPIRED"));
            identityAccessEventService.append(session.userAccountId(), session.userAccountId(), IdentityEventType.SESSION_ENDED, Map.of("reason", "EXPIRED"));
            return Optional.empty();
        }
        return userAccountRepository.findById(session.userAccountId())
                .filter(account -> account.status() == AccountStatus.ACTIVE)
                .map(account -> new AuthenticatedSessionView(session.sessionToken(), account.id(), account.email(), account.displayName(), session.expiresAt()));
    }

    private AuthException signInFailed(String subjectUserId, String normalizedEmail, String reason, HttpStatus status) {
        if (subjectUserId != null) {
            identityAccessEventService.append(subjectUserId, subjectUserId, IdentityEventType.SIGN_IN_FAILED, Map.of("reason", reason));
        }
        throw new AuthException("SIGN_IN_FAILED", "Sign-in was not successful", status);
    }

    public record SignInResult(String sessionToken, UserAccount account, Instant expiresAt) {
    }

    public record AuthenticatedSessionView(
            String sessionToken,
            String userId,
            String email,
            String displayName,
            Instant expiresAt) {
    }
}
