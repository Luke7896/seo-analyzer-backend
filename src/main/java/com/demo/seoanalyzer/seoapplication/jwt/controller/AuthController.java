package com.demo.seoanalyzer.seoapplication.jwt.controller;

import com.demo.seoanalyzer.seoapplication.Utils.BackendConstants;
import com.demo.seoanalyzer.seoapplication.jwt.dto.JwtResponse;
import com.demo.seoanalyzer.seoapplication.jwt.dto.UserResponse;
import com.demo.seoanalyzer.seoapplication.jwt.model.RefreshToken;
import com.demo.seoanalyzer.seoapplication.jwt.service.AccessTokenService;
import com.demo.seoanalyzer.seoapplication.jwt.service.RefreshTokenService;
import com.demo.seoanalyzer.seoapplication.user.UserPrincipal;
import com.demo.seoanalyzer.seoapplication.user.dto.request.LoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/api/auth" )
public class AuthController {

    private final AccessTokenService accessTokenService;

    private final RefreshTokenService refreshTokenService;

    private final AuthenticationManager authenticationManager;

    public AuthController(AccessTokenService accessTokenService, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager ) {
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping( "/login" )
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response ) {

        String emailIdentifier = loginRequest.getIdentifier( ) != null ? loginRequest.getIdentifier( ).trim( ).toLowerCase( ) : "";

        Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken( emailIdentifier, loginRequest.getPassword( ) ) );

        SecurityContextHolder.getContext( ).setAuthentication( authentication );

        UserPrincipal userPrincipal = ( UserPrincipal ) authentication.getPrincipal( );

        if ( userPrincipal == null ) {
            throw new RuntimeException( "Unable to find user principal for user '" + loginRequest.getIdentifier( ) + "'" );
        }

        String accessToken = accessTokenService.generateAccessToken( userPrincipal.getUserEmail( ) );
        RefreshToken refreshToken = refreshTokenService.createRefreshToken( userPrincipal.getUser( ) );

        ResponseCookie cookie = ResponseCookie.from( "refreshToken", refreshToken.getToken( ) )
                .httpOnly( true )
                .secure( false )
                .path( "/api/auth/refresh-token" )
                .maxAge( 7 * 24 * 60 * 60 )
                .sameSite( "Strict" )
                .build( );
        response.addHeader( HttpHeaders.SET_COOKIE, cookie.toString( ) );

        System.out.println(ResponseEntity.ok( new JwtResponse( accessToken ) ) );

        return ResponseEntity.ok( new JwtResponse( accessToken ) );
    }

    @PostMapping( "/refresh-token" )
    public ResponseEntity<?> refreshToken( @CookieValue( name = "refreshToken" ) String token ) {
        return refreshTokenService.findByToken( token )
                .map( refreshTokenService::verifyExpiration )
                .map( RefreshToken::getUser )
                .map( user -> {
                    String newAccessToken = accessTokenService.generateAccessToken( user.getEmail( ) );
                    return ResponseEntity.ok( new JwtResponse( newAccessToken ) );
                })
                .orElseThrow( ( ) -> new RuntimeException( "Refresh token not in database!") );
    }

    @GetMapping( "/me" )
    public ResponseEntity<?> getCurrentUser( Authentication authentication ) {

        if ( authentication == null || !authentication.isAuthenticated( ) ) {
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( "Not authenticated" );
        }

        UserPrincipal userPrincipal = ( UserPrincipal ) authentication.getPrincipal( );

        if ( userPrincipal == null ) {
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "User not found" );
        }

        UserResponse userResponse = new UserResponse(
                userPrincipal.getUserEmail( ),
                userPrincipal.getAuthorities( ).stream( )
                        .map( GrantedAuthority::getAuthority )
                        .findFirst( ).orElse( BackendConstants.ROLE_LEAD_STRING ),
                userPrincipal.getUserFirstName( ),
                userPrincipal.getUserLastName( ),
                userPrincipal.getUserPhoneNumber( )
        );

        return ResponseEntity.ok( userResponse );
    }

}
