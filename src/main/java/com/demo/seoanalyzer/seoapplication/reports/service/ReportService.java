package com.demo.seoanalyzer.seoapplication.reports.service;

import com.demo.seoanalyzer.seoapplication.AuditReportDetail.service.AuditReportDetailService;
import com.demo.seoanalyzer.seoapplication.Utils.BackendConstants;
import com.demo.seoanalyzer.seoapplication.Utils.BackendUtils;
import com.demo.seoanalyzer.seoapplication.exception.ResourceNotFoundException;
import com.demo.seoanalyzer.seoapplication.reports.dto.response.AuditReportResponseDTO;
import com.demo.seoanalyzer.seoapplication.reports.model.AuditReportDetail;
import com.demo.seoanalyzer.seoapplication.reports.model.Report;
import com.demo.seoanalyzer.seoapplication.reports.repository.ReportRepository;
import com.demo.seoanalyzer.seoapplication.reports.worker.AsyncAuditProcessor;
import com.demo.seoanalyzer.seoapplication.user.model.Users;
import com.demo.seoanalyzer.seoapplication.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final AsyncAuditProcessor asyncAuditProcessor;
    private final UserService userService;
    private final AuditReportDetailService auditReportDetailService;

    public ReportService( ReportRepository reportRepository, AsyncAuditProcessor asyncAuditProcessor,  UserService userService, AuditReportDetailService auditReportDetailService ) {
        this.reportRepository = reportRepository;
        this.asyncAuditProcessor = asyncAuditProcessor;
        this.userService = userService;
        this.auditReportDetailService = auditReportDetailService;
    }

    public Integer getReportStatusOnly( String id ) {
        return reportRepository.findById( id )
                .map( Report::getStatus )
                .orElseThrow( ( ) -> new RuntimeException( "Report not found for ID: '" + id + "'" ) );
    }

    public Report initiateAsyncAudit( String domain, int type, Long userId ) {

        String sanitizedDomain = BackendUtils.cleanDomain( domain );

        LocalDateTime cutOffTime = LocalDateTime.now( ).minusHours( 24 );

        Optional<Report> existingReport = reportRepository.findFirstByDomainAndUserIdAndTypeAndStatusAndCreatedAtAfterOrderByCreatedAtDesc(
                sanitizedDomain,
                userId,
                type,
                BackendConstants.REPORT_STATUS_COMPLETED,
                cutOffTime
        );

        if ( existingReport.isPresent( ) ) {
            return existingReport.get( );
        }

        Users user = userService.findUserById( userId );

        Report report = new Report( );

        report.setType( BackendConstants.REPORT_TYPE_FREE_AUDIT );
        report.setStatus( BackendConstants.REPORT_STATUS_PENDING );
        report.setUser( user );
        report.setDomain( sanitizedDomain );
        Report savedReport = reportRepository.save( report );

        asyncAuditProcessor.processReportAsync( savedReport.getId( ), sanitizedDomain );

        return savedReport;
    }


    public AuditReportResponseDTO getReportWithAuditReportDetails( String id ) {
        Report report = findById( id );
        AuditReportDetail auditReportDetail = auditReportDetailService.findByReportId( report.getId( ) );
        return mapToDTO( report, auditReportDetail );
    }

    public AuditReportResponseDTO mapToDTO( Report report, AuditReportDetail auditReportDetail ) {

        if ( report == null ) {
            return null;
        }

        AuditReportResponseDTO dto = new AuditReportResponseDTO( );

        dto.setReportId( report.getId( ) );
        dto.setStatus( report.getStatus( ) );
        dto.setCreatedAt( report.getCreatedAt( ) );
        dto.setDomain( report.getDomain( ) );
        dto.setSiteHealthScore( auditReportDetail.getSiteHealthScore( ) );
        dto.setTotalPagesCrawled( auditReportDetail.getTotalPagesCrawled( ) );
        dto.setHealthyPagesCount( auditReportDetail.getHealthyPagesCount( ) );
        dto.setBrokenPagesCount( auditReportDetail.getBrokenPagesCount( ) );
        dto.setHaveIssuesPagesCount( auditReportDetail.getHaveIssuesPagesCount( ) );
        dto.setSiteErrorsCount( auditReportDetail.getSiteErrorsCount( ) );
        dto.setSiteWarningsCount( auditReportDetail.getSiteWarningsCount( ) );
        dto.setSiteNoticesCount( auditReportDetail.getSiteNoticesCount( ) );
        dto.setRedirectPagesCount( auditReportDetail.getRedirectPagesCount( ) );
        dto.setAiSearchScore( auditReportDetail.getSiteAiSearchScore( ) );


        System.out.println( dto );
        return dto;
    }

    public List<Report> findAllFailedReports( ) {
        return reportRepository.findAllByStatus( BackendConstants.REPORT_STATUS_FAILED );
    }

    public List<Report> findAllByUserId( Long userId ) {
        return  reportRepository.findAllByUserId( userId );
    }

    @Transactional
    public void updateReportStatus( String reportId, int status ) {
        reportRepository.findById( reportId ).ifPresent( report -> {
            report.setStatus( status );
            reportRepository.save( report );
        } );
    }

    public Report findById( String id ) {
        Optional<Report> reportOptional = reportRepository.findById( id );

        if ( reportOptional.isPresent( ) ) {
            return reportOptional.get( );
        }

        throw new ResourceNotFoundException( "Unable to find report with ID: '" + id + "'" );
    }

    public Report saveReport( Report report ) {
        return reportRepository.save( report );
    }
}

