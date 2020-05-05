package com.kangmin.security.controller.rest;

import com.kangmin.security.model.Account;
import com.kangmin.security.service.AccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
@RequestMapping("api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(final AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("")
    public List<Account> getAccounts() {
        return accountService.getAll().orElse(new ArrayList<>());
    }

    @GetMapping(path = "/{accountId}")
    public Account getAccount(final @PathVariable("accountId") String accountId) {
        return accountService.getAccountById(accountId).orElse(new Account());
    }

    @GetMapping(path = "/u/{username}")
    public Account getAccountByUsername(final @PathVariable("username") String username) {
        return accountService.getAccountByUsername(username).orElse(new Account());
    }
}
