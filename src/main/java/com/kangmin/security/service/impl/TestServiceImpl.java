package com.kangmin.security.service.impl;

import com.kangmin.security.service.TestService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public final String getMessage() {
        return "Greetings from TestService!";
    }

    @Override
    public final List<String> getSkills() {
        return Arrays.asList("Java", "Spring", "Hibernate");
    }
}