package com.demo.seoanalyzer.seoapplication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class HttpClientConfig {

    @Value( "${semrush.api.base-url}" )
    private String semrushBaseUrl;

    @Bean
    public HttpClient httpClient( ) {
        return HttpClient.newBuilder( )
                .followRedirects( HttpClient.Redirect.NORMAL )
                .connectTimeout( Duration.ofSeconds( 10 ) )
                .build( );
    }

    @Bean
    public RestClient semrushRestClient(HttpClient httpClient ) {

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory( httpClient );

        return RestClient.builder( )
                .baseUrl( semrushBaseUrl )
                .requestFactory( requestFactory )
                .build( );
    }
}
