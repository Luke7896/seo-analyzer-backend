package com.demo.seoanalyzer.seoapplication.Utils;

import java.net.URI;
import java.util.Locale;

public class BackendUtils {

    public static String cleanDomain( String rawInput ) {
        if ( rawInput == null ) return null;

        String trimmed = rawInput.trim( ).toLowerCase( Locale.ROOT );

        if ( !trimmed.startsWith( "http://" ) && !trimmed.startsWith( "https://" ) ) {
            trimmed = "https://" + trimmed;
        }

        try {
            URI uri = new URI( trimmed );
            String host = uri.getHost( );

            if ( host != null ) {
                return host.startsWith( "www." ) ? host.substring( 4 ) : host;
            }
        } catch ( Exception e ) {
            // Fallback basic sanitization if URI parsing fails
        }

        return rawInput.replaceAll("^(https?://)?(www\\.)?", "" ).split("/" ) [ 0 ];
    }
}
