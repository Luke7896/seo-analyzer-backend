package com.demo.seoanalyzer.seoapplication.reports.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table( name = "audit_report_details" )
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuditReportDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_id", nullable = false, unique = true)
    private Report report;

    @Column(name = "site_health_score")
    private Integer siteHealthScore;

    @Column(name = "total_pages_crawled")
    private Integer totalPagesCrawled;

    @Column(name = "healthy_pages_count")
    private Integer healthyPagesCount;

    @Column(name = "broken_pages_count")
    private Integer brokenPagesCount;

    @Column(name = "have_issues_pages_count")
    private Integer haveIssuesPagesCount;

    @Column(name = "redirect_pages_count")
    private Integer redirectPagesCount;

    @Column(name = "site_errors_count")
    private Integer siteErrorsCount;

    @Column(name = "site_warnings_count")
    private Integer siteWarningsCount;

    @Column(name = "site_notices_count")
    private Integer siteNoticesCount;

    @Column(name = "site_ai_search_score")
    private Integer siteAiSearchScore;

    @Column(name = "semrush_project_id")
    private Long semrushProjectId;

    @Column(name = "semrush_snapshot_id")
    private String semrushSnapshotId;

}
