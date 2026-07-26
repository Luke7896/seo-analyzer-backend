package com.demo.seoanalyzer.seoapplication.reports.controller;

import com.demo.seoanalyzer.seoapplication.Utils.BackendConstants;
import com.demo.seoanalyzer.seoapplication.reports.dto.request.AuditRequestDTO;
import com.demo.seoanalyzer.seoapplication.reports.dto.response.AuditReportResponseDTO;
import com.demo.seoanalyzer.seoapplication.reports.dto.response.ReportStatusDTO;
import com.demo.seoanalyzer.seoapplication.reports.model.Report;
import com.demo.seoanalyzer.seoapplication.reports.service.ReportService;
import com.demo.seoanalyzer.seoapplication.user.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/api/reports" )
public class ReportController {

    private final ReportService reportService;

    public ReportController( ReportService reportService ) {
        this.reportService = reportService;
    }

    @GetMapping( "/{id}/status")
    public ResponseEntity<ReportStatusDTO> checkStatus( @PathVariable String id ) {
        Integer status = reportService.getReportStatusOnly( id );
        return ResponseEntity.ok( new ReportStatusDTO( id, status ) );
    }

    @PostMapping( "/audit/free" )
    @PreAuthorize( "hasAnyRole('LEAD', 'CLIENT', 'ADMIN')")
    public ResponseEntity<ReportStatusDTO> startFreeAudit( @RequestBody AuditRequestDTO auditRequestDTO, @AuthenticationPrincipal UserPrincipal userPrincipal ) {
        Report report = reportService.initiateAsyncAudit( auditRequestDTO.getDomain( ), BackendConstants.REPORT_TYPE_FREE_AUDIT, userPrincipal.getUserId( ) );

        return ResponseEntity.status( HttpStatus.ACCEPTED ).body( new ReportStatusDTO( report.getId( ), report.getStatus( ) ) );
    }

    @GetMapping( "/{id}" )
    public ResponseEntity<AuditReportResponseDTO> getFullReport( @PathVariable String id ) {
        AuditReportResponseDTO fullReport = reportService.getReportWithAuditReportDetails( id );
        return ResponseEntity.ok( fullReport );
    }
}
