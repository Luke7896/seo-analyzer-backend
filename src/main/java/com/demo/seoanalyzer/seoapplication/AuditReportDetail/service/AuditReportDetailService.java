package com.demo.seoanalyzer.seoapplication.AuditReportDetail.service;

import com.demo.seoanalyzer.seoapplication.AuditReportDetail.repository.AuditReportDetailRawRepository;
import com.demo.seoanalyzer.seoapplication.AuditReportDetail.repository.AuditReportDetailRepository;
import com.demo.seoanalyzer.seoapplication.exception.ResourceNotFoundException;
import com.demo.seoanalyzer.seoapplication.reports.model.AuditReportDetail;
import com.demo.seoanalyzer.seoapplication.reports.model.AuditReportDetailRaw;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuditReportDetailService {

    private final AuditReportDetailRepository auditReportDetailRepository;
    private final AuditReportDetailRawRepository auditReportDetailRawRepository;

    public AuditReportDetailService( AuditReportDetailRepository auditReportDetailRepository, AuditReportDetailRawRepository auditReportDetailRawRepository ) {
        this.auditReportDetailRepository = auditReportDetailRepository;
        this.auditReportDetailRawRepository = auditReportDetailRawRepository;
    }

    public AuditReportDetail findByReportId( String reportId ) {
        Optional<AuditReportDetail> reportOptional = auditReportDetailRepository.findByReportId( reportId );

        if ( reportOptional.isPresent( ) ) {
            return reportOptional.get( );
        }

        throw new ResourceNotFoundException( "Unable to find AuditReportDetail for Report ID: '" + reportId + "'" );
    }

    public void storeAuditReportDetailRawResponse( String rawResponse ) {
        AuditReportDetailRaw auditReportDetailRaw = new AuditReportDetailRaw( );
        auditReportDetailRaw.setAuditInfoRaw( rawResponse );
        auditReportDetailRawRepository.save( auditReportDetailRaw );
    }
}
