package com.demo.seoanalyzer.seoapplication.jwt.service;

import com.demo.seoanalyzer.seoapplication.jwt.model.RefreshToken;
import com.demo.seoanalyzer.seoapplication.jwt.repository.RefreshTokenRepository;
import com.demo.seoanalyzer.seoapplication.user.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value( "${app.jwt.refresh-expiration-ms}" )
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService( RefreshTokenRepository refreshTokenRepository ) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public Optional<RefreshToken> findByToken( String token ) {
        return refreshTokenRepository.findByToken( token );
    }

    @Transactional
    public RefreshToken createRefreshToken( Users user ) {

        refreshTokenRepository.deleteByUser_Id( user.getId( ) );

        refreshTokenRepository.flush( );

        RefreshToken refreshToken = new RefreshToken( );
        refreshToken.setUser( user );
        refreshToken.setExpiryDate( Instant.now( ).plusMillis( refreshTokenDurationMs ) );
        refreshToken.setToken( UUID.randomUUID( ).toString( ) );

        return refreshTokenRepository.save( refreshToken );
    }

    public RefreshToken verifyExpiration( RefreshToken token ) {

        if ( token.getExpiryDate( ).compareTo( Instant.now( ) ) < 0 ) {
            refreshTokenRepository.delete( token );
            throw new RuntimeException( "Refresh token has expired. Please sign in again" );
        }
        return token;
    }

}