package com.kangmin.security.service;

import com.kangmin.security.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    Optional<List<Account>> getAll();

    Optional<Account> createAccount(final Account account);

    Optional<Account> getAccountByUsername(final String username);

    Optional<Account> getAccountById(final String accountId);

    boolean isUsernameExist(final String username);

    boolean isEmailExist(final String email);
}
