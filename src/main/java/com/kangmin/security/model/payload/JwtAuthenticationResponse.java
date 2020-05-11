package com.kangmin.security.model.payload;

public class JwtAuthenticationResponse {

    private String jwtToken;

    public JwtAuthenticationResponse(final String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(final String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
