package com.demo.seoanalyzer.seoapplication.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank( message = "Username is required" )
    private String identifier;
    @NotBlank( message = "Password is required" )
    private String password;

    public LoginRequest( ) {}

    public LoginRequest( String identifier, String password ) {
        this.identifier = identifier;
        this.password = password;
    }

    public @NotBlank(message = "Username or Email is required") String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(@NotBlank(message = "Username or Email is required") String identifier) {
        this.identifier = identifier;
    }

    public @NotBlank(message = "Password is required") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required") String password) {
        this.password = password;
    }

}
