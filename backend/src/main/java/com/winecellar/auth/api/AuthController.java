package com.winecellar.auth.api;

import com.winecellar.auth.application.ActivationService;
import com.winecellar.auth.application.RegistrationService;
import com.winecellar.auth.application.SessionService;
import com.winecellar.config.AuthProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final ActivationService activationService;
    private final SessionService sessionService;
    private final AuthProperties authProperties;

    public AuthController(
            RegistrationService registrationService,
            ActivationService activationService,
            SessionService sessionService,
            AuthProperties authProperties) {
        this.registrationService = registrationService;
        this.activationService = activationService;
        this.sessionService = sessionService;
        this.authProperties = authProperties;
    }

    @PostMapping("/register")
    ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegistrationService.RegistrationResult result =
                registrationService.register(request.email(), request.password(), request.displayName());
        return ResponseEntity.accepted()
                .body(new RegisterResponse("PENDING_ACTIVATION", result.activationExpiresAt()));
    }

    @PostMapping("/activate")
    ResponseEntity<ActivateResponse> activate(@Valid @RequestBody ActivateRequest request) {
        activationService.activate(request.token());
        return ResponseEntity.ok(new ActivateResponse("ACTIVE"));
    }

    @PostMapping("/sign-in")
    ResponseEntity<SessionResponse> signIn(
            @Valid @RequestBody SignInRequest request,
            HttpServletResponse response) {
        SessionService.SignInResult result = sessionService.signIn(request.email(), request.password());
        response.addHeader(HttpHeaders.SET_COOKIE, sessionCookie(result.sessionToken(), result.expiresAt()).toString());
        return ResponseEntity.ok(new SessionResponse(
                new SessionUser(result.account().id(), result.account().email(), result.account().displayName()),
                result.expiresAt()));
    }

    @PostMapping("/sign-out")
    ResponseEntity<Void> signOut(@CookieValue(name = "WC_SESSION", required = false) String sessionToken, HttpServletResponse response) {
        sessionService.signOut(sessionToken);
        ResponseCookie clearedCookie = ResponseCookie.from(authProperties.sessionCookieName(), "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, clearedCookie.toString());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/session")
    SessionResponse getSession(@CookieValue(name = "WC_SESSION") String sessionToken) {
        SessionService.AuthenticatedSessionView sessionView = sessionService.requireCurrentSession(sessionToken);
        return new SessionResponse(
                new SessionUser(sessionView.userId(), sessionView.email(), sessionView.displayName()),
                sessionView.expiresAt());
    }

    private ResponseCookie sessionCookie(String sessionToken, Instant expiresAt) {
        long maxAgeSeconds = Math.max(1, expiresAt.getEpochSecond() - Instant.now().getEpochSecond());
        return ResponseCookie.from(authProperties.sessionCookieName(), sessionToken)
                .httpOnly(true)
                .path("/")
                .sameSite("Lax")
                .maxAge(maxAgeSeconds)
                .build();
    }

    public record RegisterRequest(
            @Email String email,
            @Size(min = 12) String password,
            @NotBlank String displayName) {
    }

    public record RegisterResponse(String status, Instant activationExpiresAt) {
    }

    public record ActivateRequest(@NotBlank String token) {
    }

    public record ActivateResponse(String status) {
    }

    public record SignInRequest(@Email String email, @NotBlank String password) {
    }

    public record SessionUser(String id, String email, String displayName) {
    }

    public record SessionResponse(SessionUser user, Instant expiresAt) {
    }
}
