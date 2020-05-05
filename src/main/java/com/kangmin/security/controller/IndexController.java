package com.kangmin.security.controller;

import com.kangmin.security.config.security.PasswordConfig;
import com.kangmin.security.model.Account;
import com.kangmin.security.model.ToDoItem;
import com.kangmin.security.service.AccountService;
import com.kangmin.security.service.TestService;
import com.kangmin.security.service.ToDoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

import static com.kangmin.security.model.security.WebUserRole.ADMIN;
import static com.kangmin.security.model.security.WebUserRole.NORMAL;
import static com.kangmin.security.model.security.WebUserRole.SUPER_ADMIN;

@Import({
        PasswordConfig.class,
})
@Controller
public class IndexController {

    private final AccountService accountService;
    private final ToDoItemService toDoItemService;
    private final PasswordEncoder passwordEncoder;
    private final TestService testService;

    @Autowired
    public IndexController(final AccountService accountService,
                           final ToDoItemService toDoItemService,
                           final PasswordEncoder passwordEncoder,
                           final TestService testService) {
        this.accountService = accountService;
        this.toDoItemService = toDoItemService;
        this.passwordEncoder = passwordEncoder;
        this.testService = testService;
    }

    @GetMapping({"/", "/index"})
    public String getIndexPage(final Model model) {
        model.addAttribute("message", testService.getMessage());
        model.addAttribute("skills", testService.getSkills());
        return "index";
    }

    @GetMapping("/devTest")
    public String populateTestAccount() {
        if (!accountService.getAccountByUsername("dev").isPresent()) {
            accountService.createAccount(
                    new Account(
                            "id-0000",
                            "dev",
                            passwordEncoder.encode("dev"),
                            SUPER_ADMIN.name()
                    )
            );
            accountService.createAccount(
                    new Account(
                            "id-0001",
                            "normal",
                            passwordEncoder.encode("111"),
                            NORMAL.name()
                    )
            );
            accountService.createAccount(
                    new Account(
                            "id-0002",
                            "admin",
                            passwordEncoder.encode("222"),
                            ADMIN.name()
                    )
            );
            toDoItemService.create(
                    new ToDoItem(1L, "id-0001", "task1", new Date())
            );
            toDoItemService.create(
                    new ToDoItem(2L, "id-0002", "task2", new Date())
            );
        }
        return "redirect:/";
    }
}
