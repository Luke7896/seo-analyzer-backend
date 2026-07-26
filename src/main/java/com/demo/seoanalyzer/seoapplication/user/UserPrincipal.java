package com.demo.seoanalyzer.seoapplication.user;

import com.demo.seoanalyzer.seoapplication.Utils.BackendConstants;
import com.demo.seoanalyzer.seoapplication.user.model.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    private Users user;

    public UserPrincipal( Users user ) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities( ) {

        if ( user.getRole( ) == BackendConstants.ROLE_LEAD ) {
            return Collections.singleton( new SimpleGrantedAuthority( BackendConstants.ROLE_LEAD_STRING ) );
        }

        if ( user.getRole( ) == BackendConstants.ROLE_CLIENT ) {
            return Collections.singleton( new SimpleGrantedAuthority( BackendConstants.ROLE_CLIENT_STRING ) );
        }

        if ( user.getRole( ) == BackendConstants.ROLE_ADMIN ) {
            return Collections.singleton( new SimpleGrantedAuthority( BackendConstants.ROLE_ADMIN_STRING ) );
        }

        if ( user.getRole( ) == BackendConstants.ROLE_FORMER_CLIENT ) {
            return  Collections.singleton( new SimpleGrantedAuthority( BackendConstants.ROLE_FORMER_CLIENT_STRING ) );
        }

        throw new RuntimeException( "Unable to determine user role for user '" + user.getEmail( ) + "'" );
    }

    @Override
    public String getPassword( ) {
        return user.getPassword( );
    }

    @Override
    public String getUsername( ) {
        return user.getEmail( );
    }

    @Override
    public boolean isEnabled( ) {
        return user.getActive( ) == 1;
    }

    public Users getUser( ) {
        return user;
    }

    public void setUser( Users user ) {
        this.user = user;
    }

    public Long getUserId( ) {
        return user.getId( );
    }

    public String getUserEmail(){
        return user.getEmail( );
    }

    public String getUserFirstName( ) {
        return user.getFirstName( );
    }

    public String getUserLastName(){
        return user.getLastName();
    }

    public String getUserPhoneNumber(){
        return user.getPhoneNumber();
    }

}
