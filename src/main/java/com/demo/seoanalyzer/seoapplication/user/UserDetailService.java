package com.demo.seoanalyzer.seoapplication.user;

import com.demo.seoanalyzer.seoapplication.user.model.Users;
import com.demo.seoanalyzer.seoapplication.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Users> userOptional = userRepository.findByEmailIgnoreCase( email );

        if ( userOptional.isPresent( ) ) {
            return new UserPrincipal( userOptional.get( ) );
        }

        throw new UsernameNotFoundException( "User not found with email: '" + email + "'" );
    }
}