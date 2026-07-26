package com.demo.seoanalyzer.seoapplication.config;

import com.demo.seoanalyzer.seoapplication.jwt.JwtFilter;
import com.demo.seoanalyzer.seoapplication.user.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailService userDetailService;
    private final JwtFilter jwtFilter;

    public SecurityConfig( UserDetailService userDetailService, JwtFilter jwtFilter ) {
        this.userDetailService = userDetailService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception {

        return http.csrf( AbstractHttpConfigurer::disable )
                .cors( c -> c.configurationSource( corsConfigurationSource( ) ) )
                .authorizeHttpRequests( request -> request
                        .requestMatchers("/api/register",
                                "/api/auth/login",
                                "/api/auth/refresh-token",
                                "/api/stripe/webhook").permitAll( )
                        .anyRequest( ).authenticated( ) )
                .httpBasic( Customizer.withDefaults( ) )
                .sessionManagement( session -> session.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
                .addFilterBefore( jwtFilter, UsernamePasswordAuthenticationFilter.class )
                .build( );
    }

    @Bean
    public AuthenticationProvider authenticationProvider( ) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider( userDetailService );
        provider.setPasswordEncoder( new BCryptPasswordEncoder(12 ) );

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config ) throws Exception {
        return config.getAuthenticationManager( );
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource( ) {

        CorsConfiguration configuration = new CorsConfiguration( );
        configuration.setAllowedOrigins(Arrays.asList( "http://localhost:5174", "http://localhost:5173", "http://localhost:3000") );
        configuration.setAllowedMethods( Arrays.asList( "GET", "POST", "PUT", "DELETE", "OPTIONS" ) );
        configuration.setAllowedHeaders( Arrays.asList( "*" ) );
        configuration.setAllowCredentials( true );
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource( );
        source.registerCorsConfiguration( "/api/**", configuration );

        return source;
    }

}
