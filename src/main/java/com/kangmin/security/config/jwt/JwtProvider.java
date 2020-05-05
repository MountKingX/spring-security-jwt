package com.kangmin.security.config.jwt;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Date;

@Configuration
public class JwtProvider {

    @Value("${webapp.jwt.secretKeyStr}")
    private String secretKeyStr;

    @Value("${webapp.jwt.tokenPrefix}")
    private String tokenPrefix;

    @Value("${webapp.jwt.tokenExpirationAfterDays}")
    private Integer tokenExpirationAfterDays;

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public Integer getTokenExpirationAfterDays() {
        return tokenExpirationAfterDays;
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    public SecretKey getShaSecretKey() {
        return Keys.hmacShaKeyFor(this.secretKeyStr.getBytes());
    }

    public String generateToken(final Authentication auth) {
        return Jwts.builder()
                .setSubject(auth.getName())
                .claim("authorities", auth.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(getTokenExpirationAfterDays())))
                .signWith(getShaSecretKey())
                .compact();
    }

    public Jws<Claims> validateToken(final String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getShaSecretKey())
                    .build()
                    .parseClaimsJws(token);

        } catch (final SignatureException e) {
            throw new SignatureException("Invalid JWT signature");

        } catch (final MalformedJwtException e) {
            throw new MalformedJwtException("Invalid JWT token");

        } catch (final ExpiredJwtException e) {
            e.printStackTrace();
            throw new IllegalStateException("Expired JWT token");

        } catch (final UnsupportedJwtException e) {
            throw new UnsupportedJwtException("Unsupported JWT token");

        } catch (final JwtException e) {
            throw new IllegalStateException("Token %s cannot be trusted");
        }
    }
}
