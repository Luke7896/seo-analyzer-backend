package com.demo.seoanalyzer.seoapplication.reports.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Component
public class SemrushApiClient {

    private final RestClient restClient;

    public SemrushApiClient( RestClient semrushRestClient ) {
        this.restClient = semrushRestClient;
    }

    @Value( "${semrush.api.key}" )
    private String apiKey;

    public String createProject( String domain, String projectName ) {

        System.out.println( "Creating Project -> Domain: " + domain + " projectName: " + projectName );

        Map<String, String> projectPayload = new HashMap<>( );

        projectPayload.put( "project_name", projectName );
        projectPayload.put( "url", domain );

        return restClient.post( )
                .uri( uriBuilder -> uriBuilder
                        .path( "/management/v1/projects" )
                        .queryParam("key", apiKey )
                        .build( ) )
                .contentType( MediaType.APPLICATION_JSON )
                .body( projectPayload )
                .retrieve( )
                .onStatus( HttpStatusCode::isError, (request, response ) -> {
                    String errorBody = new String( response.getBody( ).readAllBytes( ) );
                    System.err.println( "SEMrush API Error Status: " + response.getStatusCode( ) );
                    System.err.println( "SEMrush API Error Response: " + errorBody );
                    throw new RuntimeException("SEMrush project creation failed: " + errorBody );
                } )
                .body( String.class );
    }

    public String enableSiteAudit( Long projectId, String domain ) {

        System.out.println( "Enabling site audit -> projectId: " + projectId + " domain: " + domain );

        Map<String, Object> body = Map.of(
                "domain", domain,
                "scheduleDay", 0,
                "notify", false,
                "pageLimit", 1000,
                "userAgentType", 2,
                "crawlSubdomains", true,
                "respectCrawlDelay", false
        );

        return restClient.post( )
                .uri( uriBuilder -> uriBuilder.path("/management/v1/projects/{id}/siteaudit/enable" )
                        .queryParam("key", apiKey )
                        .build( projectId ) )
                .body( body )
                .retrieve( )
                .body( String.class );
    }

    public String launchSiteAudit( Long projectId ) {

        System.out.println( "Launching site audit -> projectId: " + projectId );

        return restClient.post( ).uri( uriBuilder -> uriBuilder.path( "/reports/v1/projects/{id}/siteaudit/launch" )
                        .queryParam("key", apiKey ).build( projectId ) )
                .retrieve( )
                .body( String.class );
    }

    public String fetchAuditCampaignInfo( Long projectId ) {

        System.out.println( "Fetching audit campaign info -> projectId: " + projectId);

        return restClient.get( ).uri( uriBuilder -> uriBuilder.path( "/reports/v1/projects/{id}/siteaudit/info" )
                        .queryParam( "key", apiKey )
                        .build( projectId ) )
                .retrieve( )
                .body( String.class );
    }

    public String fetchPaidAuditInfo( Long projectId, String snapshotId ) {

        return restClient.get( ).uri( uriBuilder -> uriBuilder.path( "/reports/v1/projects/{id}/siteaudit/snapshot" )
                        .queryParam( "key", apiKey )
                        .queryParam( "snapshot_id", snapshotId )
                        .build( projectId ) )
                .retrieve( )
                .body( String.class );
    }

    public void deleteSemrushProjectById( Long projectId ) {

        restClient.delete( ).uri( uriBuilder -> uriBuilder.path( "/management/v1/projects/{id}" )
                .queryParam( "key", apiKey )
                .build( projectId )).retrieve().onStatus( HttpStatusCode::isError, ( (request, response) -> {
                    String errorBody = new String( response.getBody( ).readAllBytes( ) );
                    System.err.println( "SEMrush API Delete Error Status: " + response.getStatusCode( ) );
                    System.err.println( "SEMrush API Delete Error Response: " + errorBody );
                    throw new RuntimeException( "SEMrush project deletion failed: " + errorBody );
        }) ).toBodilessEntity( );
    }

}
