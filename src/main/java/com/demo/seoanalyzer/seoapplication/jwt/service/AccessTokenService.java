package com.demo.seoanalyzer.seoapplication.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class AccessTokenService {

    private String secretKey = "";

    public AccessTokenService( ) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance( "HmacSHA256" );
            SecretKey sk = keyGenerator.generateKey( );
            secretKey = Base64.getEncoder( ).encodeToString( sk.getEncoded( ) );
        } catch ( NoSuchAlgorithmException e ) {
            throw new RuntimeException( e );
        }
    }

    public String generateAccessToken( String username ) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder( )
                .claims( )
                .add( claims )
                .subject( username )
                .issuedAt( new Date( System.currentTimeMillis( ) ) )
                .expiration( new Date( System.currentTimeMillis( ) + 1000 * 60 * 15 ) )
                .and( )
                .signWith( getKey( ) )
                .compact( );
    }

    private SecretKey getKey( ) {
        byte[] keyBytes = Decoders.BASE64.decode( secretKey );
        return Keys.hmacShaKeyFor( keyBytes );
    }

    public String extractUsername( String token ) {
        return extractClaim( token, Claims::getSubject );
    }

    public boolean validateAccessToken( String token, UserDetails userDetails ) {

        final String userName = extractUsername( token );

        return ( userName.equals( userDetails.getUsername( ) ) && !isAccessTokenExpired( token ) );
    }

    private Claims extractAllClaims( String token ) {
        return Jwts.parser( )
                .verifyWith( getKey( ) )
                .build( )
                .parseSignedClaims( token )
                .getPayload( );
    }

    private boolean isAccessTokenExpired(String token ) {
        return extractExpiration( token ).before( new Date( ) );
    }

    private Date extractExpiration( String token ) {
        return extractClaim( token, Claims::getExpiration );
    }

    private <T> T extractClaim( String token, Function<Claims, T> claimResolver ) {
        final Claims claims = extractAllClaims( token );
        return claimResolver.apply( claims );
    }
}

