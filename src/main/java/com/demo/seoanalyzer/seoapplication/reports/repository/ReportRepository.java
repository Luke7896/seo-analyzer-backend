package com.demo.seoanalyzer.seoapplication.reports.repository;

import com.demo.seoanalyzer.seoapplication.reports.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, String> {

    Optional<Report> findFirstByDomainAndUserIdAndTypeAndStatusAndCreatedAtAfterOrderByCreatedAtDesc(
            String domain,
            Long userId,
            int tier,
            int status,
            LocalDateTime cutoffTime
    );

    List<Report> findAllByStatus( int status );

    Optional<Report> findById( String id );

    List<Report> findAllByUserId( Long userId );
}