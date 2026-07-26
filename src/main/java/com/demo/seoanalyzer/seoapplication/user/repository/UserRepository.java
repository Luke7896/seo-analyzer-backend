package com.demo.seoanalyzer.seoapplication.user.repository;

import com.demo.seoanalyzer.seoapplication.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmailIgnoreCase( String email );

    Optional< Users > findById( Long id );
}

