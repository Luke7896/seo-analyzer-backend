package com.demo.seoanalyzer.seoapplication.user.service;

import com.demo.seoanalyzer.seoapplication.Utils.BackendConstants;
import com.demo.seoanalyzer.seoapplication.jwt.service.AccessTokenService;
import com.demo.seoanalyzer.seoapplication.user.dto.request.LoginRequest;
import com.demo.seoanalyzer.seoapplication.user.dto.request.RegisterRequest;
import com.demo.seoanalyzer.seoapplication.user.model.Users;
import com.demo.seoanalyzer.seoapplication.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final AccessTokenService jwtService;

    @Value( "${app.frontend-url}" )
    private String frontendBaseUrl;


    public UserService( UserRepository userRepository, AuthenticationManager authManager, AccessTokenService jwtService ) {
        this.userRepository = userRepository;
        this.authManager = authManager;
        this.jwtService = jwtService;

    }

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder( 12 );

    public Users register( RegisterRequest registerRequest ) {

        if ( registerRequest.getEmail() != null && userRepository.findByEmailIgnoreCase( registerRequest.getEmail( ) ).isPresent( ) ) {
            throw new IllegalArgumentException( "Email already exists" );
        }

        Users user = new Users( );

        user.setEmail( registerRequest.getEmail( ) );
        user.setPassword( encoder.encode( registerRequest.getPassword( ) ) );
        user.setCreated_at( now( ) );
        user.setUpdated_at( now( ) );

        user.setActive( BackendConstants.USER_ACTIVE );
        user.setFirstName( registerRequest.getFirstName( ) );
        user.setLastName( registerRequest.getLastName( ) );
        user.setPhoneNumber( registerRequest.getPhoneNumber( ) );

        return userRepository.save( user );
    }

    public String verify( LoginRequest loginRequest ) {

        Authentication auth = authManager.authenticate( new UsernamePasswordAuthenticationToken( loginRequest.getIdentifier( ), loginRequest.getPassword( ) ) );

        if ( auth.isAuthenticated( ) ) {
            return jwtService.generateAccessToken( loginRequest.getIdentifier( ) );
        }

        return "Failed";
    }

    public void deleteUserById( Long id ) {
        userRepository.deleteById( id );
    }


    public Users findUserByEmail( String email ) {
        Optional<Users> userOptional = userRepository.findByEmailIgnoreCase( email );

        if ( userOptional.isPresent( ) ) {
            return  userOptional.get( );
        }

        throw new RuntimeException( "User not found with email: '" + email + "'" );
    }

    public Users findUserById( Long id ) {
        Optional<Users> userOptional = userRepository.findById( id );

        if ( userOptional.isPresent( ) ) {
            return userOptional.get( );
        }

        throw new RuntimeException("User not found with email: '" + id + "'" );
    }

    public void updateUserActiveStatus( Long id, int status ) {
        Optional<Users> userToUpdate = userRepository.findById( id );

        if ( userToUpdate.isPresent( ) ) {
            Users user = userToUpdate.get( );
            user.setActive( status );

            userRepository.save( user );
        }

        throw new RuntimeException( "Unable to find user with ID: '" + id + "'");
    }

}