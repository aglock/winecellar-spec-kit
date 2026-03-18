package com.winecellar.config;

import com.winecellar.auth.application.SessionService;
import com.winecellar.auth.application.SessionService.AuthenticatedSessionView;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class CurrentUserFilter extends OncePerRequestFilter {

    private final AuthProperties authProperties;
    private final SessionService sessionService;

    public CurrentUserFilter(AuthProperties authProperties, SessionService sessionService) {
        this.authProperties = authProperties;
        this.sessionService = sessionService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        resolveSessionToken(request.getCookies())
                .flatMap(sessionService::authenticate)
                .ifPresent(this::authenticateUser);

        filterChain.doFilter(request, response);
    }

    private Optional<String> resolveSessionToken(Cookie[] cookies) {
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> authProperties.sessionCookieName().equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    private void authenticateUser(AuthenticatedSessionView sessionView) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        sessionView.userId(),
                        sessionView.sessionToken(),
                        AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
