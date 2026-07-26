package com.demo.seoanalyzer.seoapplication.user.controller;

import com.demo.seoanalyzer.seoapplication.user.dto.request.LoginRequest;
import com.demo.seoanalyzer.seoapplication.user.dto.request.RegisterRequest;
import com.demo.seoanalyzer.seoapplication.user.model.Users;
import com.demo.seoanalyzer.seoapplication.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController( UserService userService ) {
        this.userService= userService;
    }

    @PostMapping( "/api/register" )
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest ) {

        Users registeredUser = userService.register( registerRequest );
        return new ResponseEntity<>( registeredUser, HttpStatus.CREATED );
    }

    @PostMapping("/api/login")
    public String login( @RequestBody LoginRequest loginRequest ) {
        return userService.verify( loginRequest );
    }


}
