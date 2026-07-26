package com.demo.seoanalyzer.seoapplication.reports.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table( name = "audit_report_detail_raw" )
@Getter
@Setter
public class AuditReportDetailRaw {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private String id;

    @MapsId
    @OneToOne( fetch = FetchType.LAZY )
    @JoinColumn( name = "report" )
    private Report report;

    @Lob
    @Column( columnDefinition = "LONGTEXT")
    private String auditInfoRaw;

    public AuditReportDetailRaw( ) {

    }

    public AuditReportDetailRaw( Report report ) {
        this.report = report;
    }
}
