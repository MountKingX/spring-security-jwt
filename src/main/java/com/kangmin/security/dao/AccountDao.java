package com.kangmin.security.dao;

import com.kangmin.security.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDao extends JpaRepository<Account, Long> {

    Optional<Account> findAccountByUsername(final String username);

    Optional<Account> findAccountByAccountId(final String accountId);
}