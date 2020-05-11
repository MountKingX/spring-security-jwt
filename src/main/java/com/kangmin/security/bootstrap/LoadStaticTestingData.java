package com.kangmin.security.bootstrap;

import com.kangmin.security.model.Account;
import com.kangmin.security.model.ToDoItem;
import com.kangmin.security.service.AccountService;
import com.kangmin.security.service.ToDoItemService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.kangmin.security.model.security.WebUserRole.ADMIN;
import static com.kangmin.security.model.security.WebUserRole.NORMAL;
import static com.kangmin.security.model.security.WebUserRole.SUPER_ADMIN;

@Component
public class LoadStaticTestingData implements CommandLineRunner {

    private final AccountService accountService;
    private final ToDoItemService toDoItemService;
    private final PasswordEncoder passwordEncoder;

    public LoadStaticTestingData(final AccountService accountService,
                                 final ToDoItemService toDoItemService,
                                 final PasswordEncoder passwordEncoder) {
        this.accountService = accountService;
        this.toDoItemService = toDoItemService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(final String... args) {
        if (!accountService.getAccountByUsername("dev").isPresent()) {
            accountService.createAccount(
                    new Account(
                            "id-0000",
                            "dev",
                            passwordEncoder.encode("dev"),
                            SUPER_ADMIN.name(),
                            "Super-Admin User",
                            "superadmin@security.com"
                    )
            );
            accountService.createAccount(
                    new Account(
                            "id-0111",
                            "normal",
                            passwordEncoder.encode("111"),
                            NORMAL.name(),
                            "Normal User",
                            "normal111@security.com"
                    )
            );
            accountService.createAccount(
                    new Account(
                            "id-0222",
                            "admin",
                            passwordEncoder.encode("222"),
                            ADMIN.name(),
                            "Admin User",
                            "admin222@security.com"
                    )
            );
            toDoItemService.create(
                    new ToDoItem(1L, "id-0111", "task1", new Date())
            );
            toDoItemService.create(
                    new ToDoItem(2L, "id-0222", "task2", new Date())
            );
        }
    }
}
