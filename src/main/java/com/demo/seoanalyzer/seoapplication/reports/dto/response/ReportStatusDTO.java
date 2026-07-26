package com.demo.seoanalyzer.seoapplication.reports.dto.response;

public class ReportStatusDTO {

    private String reportId;
    private int status;

    public ReportStatusDTO( String reportId, int status ) {
        this.reportId = reportId;
        this.status = status;
    }

    public String getReportId( ) {
        return reportId;
    }

    public void setReportId( String reportId ) {
        this.reportId = reportId;
    }

    public int getStatus( ) {
        return status;
    }

    public void setStatus( int status ) {
        this.status = status;
    }
}
