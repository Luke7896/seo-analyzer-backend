package com.demo.seoanalyzer.seoapplication.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {

    @NotBlank( message = "Email is required!" )
    private String email;
    @NotBlank( message = "Password is required!" )
    private String password;
    @NotBlank( message = "First name is required!")
    private String firstName;
    @NotBlank( message = "Last name is required!")
    private String lastName;
    @NotBlank( message = "Phone number is required!")
    private String phoneNumber;

    public RegisterRequest(String email, String password, String firstName, String lastName, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;

    }

    public RegisterRequest() {
    }

    public @NotBlank(message = "Email is required") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email is required") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Password is required") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required") String password) {
        this.password = password;
    }

    public @NotBlank(message = "First name is required!") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "First name is required!") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "Last name is required!") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "Last name is required!") String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank(message = "Phone number is required!") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank(message = "Phone number is required!") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}

