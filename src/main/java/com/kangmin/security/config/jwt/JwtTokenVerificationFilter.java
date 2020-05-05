package com.kangmin.security.config.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenVerificationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtTokenVerificationFilter(final JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(jwtProvider.getAuthorizationHeader());
        if (Strings.isNullOrEmpty(authorizationHeader)
                || !authorizationHeader.startsWith(jwtProvider.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authorizationHeader.replace(jwtProvider.getTokenPrefix(), "");
        try {
            final Jws<Claims> claimsJws = jwtProvider.validateToken(token);
            final Claims body = claimsJws.getBody();
            final String username = body.getSubject();

            @SuppressWarnings("unchecked")
            final List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");
            final Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            final Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    simpleGrantedAuthorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (final JwtException e) {
            throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
        }

        filterChain.doFilter(request, response);
    }
}
