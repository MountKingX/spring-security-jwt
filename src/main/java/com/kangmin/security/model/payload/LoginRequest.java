package com.kangmin.security.model.payload;

public class LoginRequest {

    private String username;
    private String password;

    public LoginRequest() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
