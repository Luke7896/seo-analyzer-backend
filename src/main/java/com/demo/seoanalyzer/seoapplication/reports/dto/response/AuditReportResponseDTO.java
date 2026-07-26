package com.demo.seoanalyzer.seoapplication.reports.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuditReportResponseDTO {

    private String reportId;
    private int status;
    private LocalDateTime createdAt;
    private String domain;
    private int siteHealthScore;
    private int totalPagesCrawled;
    private int healthyPagesCount;
    private int brokenPagesCount;
    private int haveIssuesPagesCount;
    private int redirectPagesCount;
    private int siteErrorsCount;
    private int siteWarningsCount;
    private int siteNoticesCount;
    private int aiSearchScore;

}
