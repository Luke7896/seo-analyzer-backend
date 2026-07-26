package com.demo.seoanalyzer.seoapplication.reports.model;

import com.demo.seoanalyzer.seoapplication.user.model.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table( name = "report" )
@Getter
@Setter
public class Report {

    @Id
    @Column( name = "id", length = 36, nullable = false, updatable = false )
    private String id;

    @Column( nullable = false )
    private int status;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "users_id", nullable = false )
    private Users user;

    @Column( name = "created_at", nullable = false, updatable = false )
    private LocalDateTime createdAt;

    @Column( name = "type" )
    private int type;

    @Column( name = "domain" )
    private String domain;

    @Column( name = "failure_reason")
    private String failureReason;

    public Report( ) {
        this.id = UUID.randomUUID( ).toString( );
        this.createdAt = LocalDateTime.now( );
    }

}
