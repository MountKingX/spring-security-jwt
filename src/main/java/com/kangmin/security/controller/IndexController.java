package com.kangmin.security.controller;

import com.kangmin.security.config.security.PasswordConfig;
import com.kangmin.security.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Import({
        PasswordConfig.class,
})
@Controller
public class IndexController {

    private final GreetingService greetingService;

    @Autowired
    public IndexController(final GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping({"/", "/index"})
    public String getIndexPage(final Model model) {
        model.addAttribute("message", greetingService.getMessage());
        model.addAttribute("skills", greetingService.getSkills());
        return "index";
    }
}
