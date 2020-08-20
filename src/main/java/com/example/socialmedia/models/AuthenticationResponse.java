package com.example.socialmedia.models;

public class AuthenticationResponse {

    private final String jwt;
    private final long id;

    public AuthenticationResponse(String jwt, long id) {
        this.jwt = jwt;
        this.id = id;
    }

    public String getJwt() {
        return jwt;
    }

    public long getId() {
        return id;
    }
}
