package com.demo.seoanalyzer.seoapplication.AuditReportDetail.repository;

import com.demo.seoanalyzer.seoapplication.reports.model.AuditReportDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuditReportDetailRepository extends JpaRepository<AuditReportDetail, Long> {

    Optional<AuditReportDetail> findByReportId( String reportId );
}
