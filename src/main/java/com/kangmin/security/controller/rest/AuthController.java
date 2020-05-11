package com.kangmin.security.controller.rest;

import com.kangmin.security.config.jwt.JwtProvider;
import com.kangmin.security.model.Account;
import com.kangmin.security.model.payload.ApiResponse;
import com.kangmin.security.model.payload.JwtAuthenticationResponse;
import com.kangmin.security.model.payload.LoginRequest;
import com.kangmin.security.model.payload.RegisterRequest;
import com.kangmin.security.model.security.WebUserRole;
import com.kangmin.security.service.AccountService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthController(final AccountService accountService,
                          final AuthenticationManager authenticationManager,
                          final JwtProvider jwtProvider,
                          final PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(final @Valid @RequestBody LoginRequest loginRequest) {

        final Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        try {
            final Authentication authResult = authenticationManager.authenticate(authentication);
            final String token = jwtProvider.generateToken(authResult);
            final HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set(jwtProvider.getAuthorizationHeader(), jwtProvider.getTokenPrefix() + token);
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(new JwtAuthenticationResponse(token));
            // return new ResponseEntity<>(new JwtAuthenticationResponse(token), HttpStatus.OK);

        } catch (final AuthenticationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ApiResponse(false, "Username/Password not Authenticated!"),
                    HttpStatus.BAD_REQUEST
            );
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(final @Valid @RequestBody RegisterRequest request) {

        if (accountService.isUsernameExist(request.getUsername())) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (accountService.isEmailExist(request.getEmail())) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST
            );
        }

        final String uuid = UUID.randomUUID().toString();
        final Account account = new Account(
                uuid,
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                WebUserRole.NORMAL.name(),
                request.getName(),
                request.getEmail()
        );

        final Optional<Account> createdOpt = accountService.createAccount(account);
        if (createdOpt.isPresent()) {
            final URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/api/accounts/u/{username}")
                    .buildAndExpand(account.getUsername()).toUri();

            return ResponseEntity.created(location).body(
                    new ApiResponse(true, "User registered successfully")
            );
        }

        return new ResponseEntity<>(
                new ApiResponse(false, "Error happened during account creation!"),
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }
}
