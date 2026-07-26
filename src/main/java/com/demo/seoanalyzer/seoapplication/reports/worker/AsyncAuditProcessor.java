package com.demo.seoanalyzer.seoapplication.reports.worker;

import com.demo.seoanalyzer.seoapplication.Utils.BackendConstants;
import com.demo.seoanalyzer.seoapplication.exception.ResourceNotFoundException;
import com.demo.seoanalyzer.seoapplication.reports.client.SemrushApiClient;
import com.demo.seoanalyzer.seoapplication.reports.model.AuditReportDetail;
import com.demo.seoanalyzer.seoapplication.reports.model.AuditReportDetailRaw;
import com.demo.seoanalyzer.seoapplication.reports.model.Report;
import com.demo.seoanalyzer.seoapplication.AuditReportDetail.repository.AuditReportDetailRawRepository;
import com.demo.seoanalyzer.seoapplication.AuditReportDetail.repository.AuditReportDetailRepository;
import com.demo.seoanalyzer.seoapplication.reports.repository.ReportRepository;
import com.demo.seoanalyzer.seoapplication.reports.service.ReportService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class AsyncAuditProcessor {


    private final ReportRepository reportRepository;
    private final AuditReportDetailRawRepository auditReportDetailRawRepository;
    private final AuditReportDetailRepository auditReportDetailRepository;
    private final SemrushApiClient semrushApiClient;
    private final ObjectMapper mapper = new ObjectMapper( );


    public AsyncAuditProcessor(ReportRepository reportRepository, SemrushApiClient semrushApiClient, AuditReportDetailRawRepository auditReportDetailRawRepository, AuditReportDetailRepository auditReportDetailRepository ) {
        this.reportRepository = reportRepository;
        this.semrushApiClient = semrushApiClient;
        this.auditReportDetailRawRepository = auditReportDetailRawRepository;
        this.auditReportDetailRepository = auditReportDetailRepository;
    }

    @Async( "reportTaskExecutor" )
    public void processReportAsync( String reportId, String domain ) {

        updateReportStatus( reportId, BackendConstants.REPORT_STATUS_PROCESSING );

        try {

            long projectId = getSemrushProjectId( domain );

            semrushApiClient.enableSiteAudit( projectId, domain );

            String launchRaw = semrushApiClient.launchSiteAudit( projectId );

            String snapShotId = getSnapshotId( launchRaw );

            String infoRaw = getAuditReport( projectId );

            Report report = reportRepository.findById( reportId )
                    .orElseThrow( ( ) -> new ResourceNotFoundException( "Unable to find report with ID: '" + reportId + "'" ) );

            storeRawAuditReportResponse( infoRaw, report );

            populateAuditReportDetail( infoRaw, report, projectId, snapShotId );

            report.setStatus( BackendConstants.REPORT_STATUS_COMPLETED );
            reportRepository.save( report );

            semrushApiClient.deleteSemrushProjectById( projectId );

        } catch ( Exception e ) {
            e.printStackTrace( );
            updateReportStatus( reportId, BackendConstants.REPORT_STATUS_FAILED );
        }

    }

    public String getAuditReport( Long projectId ) throws InterruptedException {
        boolean activeCrawl = true;
        String infoRaw = "";
        int loopBreaker = 0;

        while ( activeCrawl && loopBreaker < 30 ) {

            Thread.sleep( 20000 );
            infoRaw = semrushApiClient.fetchAuditCampaignInfo( projectId );
            JsonNode infoJson = mapper.readTree( infoRaw );

            if ( "FINISHED".equalsIgnoreCase( infoJson.get( "status" ).asString( ) ) ) {
                activeCrawl = false;
            }
            loopBreaker++;
        }

        return infoRaw;
    }

    public String getSnapshotId( String auditLaunchRaw ) {
        JsonNode launchNodes = mapper.readTree( auditLaunchRaw );
        JsonNode snapShotNode = launchNodes.path( "snapshot_id" );

        if ( snapShotNode.isMissingNode( ) ) {
            throw new RuntimeException( "Unable to determine snapshot_id" );
        }

        return snapShotNode.asString( );
    }

    public void populateAuditReportDetail( String infoRaw, Report report, long projectId, String snapShotId ) {

        AuditReportDetail reportDetail = new AuditReportDetail( );

        reportDetail.setReport( report );

        JsonNode infoNode = mapper.readTree( infoRaw );

        reportDetail.setSemrushProjectId( projectId );
        reportDetail.setSemrushSnapshotId( snapShotId );

        reportDetail.setTotalPagesCrawled( infoNode.get( "pages_crawled" ).asInt( ) );
        reportDetail.setHealthyPagesCount( infoNode.get( "healthy" ).asInt( ) );
        reportDetail.setBrokenPagesCount( infoNode.get( "broken" ).asInt( ) );
        reportDetail.setHaveIssuesPagesCount( infoNode.get( "haveIssues" ).asInt( ) );
        reportDetail.setSiteErrorsCount( infoNode.get( "errors" ).asInt( ) );
        reportDetail.setSiteWarningsCount( infoNode.get( "warnings" ).asInt( ) );
        reportDetail.setSiteNoticesCount( infoNode.get( "notices" ).asInt( ) );
        reportDetail.setRedirectPagesCount( infoNode.get( "redirected" ).asInt( ) );

        JsonNode snapshotNode = infoNode.path( "current_snapshot" );
        if ( !snapshotNode.isMissingNode( ) ) {
            int siteHealth = snapshotNode.path( "quality" ).path( "value" ).asInt( 0 );
            int aiSearchScore = snapshotNode.path( "aiSearchScore" ).path( "value" ).asInt( 0 );

            reportDetail.setSiteHealthScore( siteHealth );
            reportDetail.setSiteAiSearchScore( aiSearchScore );
        }

        auditReportDetailRepository.save( reportDetail );
    }

    public void storeRawAuditReportResponse( String infoRaw, Report report ) {
        AuditReportDetailRaw auditReportDetailRaw = new AuditReportDetailRaw( report );
        auditReportDetailRaw.setAuditInfoRaw( infoRaw );

        auditReportDetailRawRepository.save( auditReportDetailRaw );
    }

    public Long getSemrushProjectId( String domain ) {
        String projectRaw = semrushApiClient.createProject( domain, "Audit-TEST" );

        JsonNode rootNode = mapper.readTree( projectRaw );
        JsonNode idNode = rootNode.path("project_id" );

        if ( idNode.isMissingNode( ) ) {
            throw new RuntimeException( "Failed to create SEMrush project. Raw Response: " + projectRaw );
        }

        return idNode.asLong( );
    }

    private void updateReportStatus( String reportId, int status ) {
        reportRepository.findById( reportId ).ifPresent( report -> {
            report.setStatus( status );
            reportRepository.save( report );
        } );
    }

}
