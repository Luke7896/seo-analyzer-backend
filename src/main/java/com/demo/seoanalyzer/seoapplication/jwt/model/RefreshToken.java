package com.demo.seoanalyzer.seoapplication.jwt.model;

import com.demo.seoanalyzer.seoapplication.user.model.Users;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @OneToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private Users user;

    public RefreshToken () {

    }

    public RefreshToken(Long id, String token, Instant expiryDate, Users user) {
        this.id = id;
        this.token = token;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
