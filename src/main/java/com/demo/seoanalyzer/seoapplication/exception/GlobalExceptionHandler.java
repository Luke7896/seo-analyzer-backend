package com.demo.seoanalyzer.seoapplication.exception;

import jakarta.persistence.NonUniqueResultException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler( MethodArgumentNotValidException.class )
    public ResponseEntity< ErrorResponse > handleValidation( MethodArgumentNotValidException ex, HttpServletRequest request ) {

        String message = ex.getBindingResult( ).getFieldErrors( ).stream( )
                .findFirst( )
                .map( DefaultMessageSourceResolvable::getDefaultMessage )
                .orElse( "Validation failed" );

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value( ),
                "Bad Request",
                message,
                request.getRequestURI( )
        );

        return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( error );
    }

    @ExceptionHandler( IllegalArgumentException.class )
    public ResponseEntity<ErrorResponse> handleIllegalArgument( IllegalArgumentException ex, HttpServletRequest request ) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value( ),
                "Conflict",
                ex.getMessage( ),
                request.getRequestURI( )
        );

        return ResponseEntity.status( HttpStatus.CONFLICT ).body( error );
    }

    @ExceptionHandler( BadCredentialsException.class )
    public ResponseEntity<ErrorResponse> handleBadCredentials( BadCredentialsException ex, HttpServletRequest request ) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value( ),
                "Unauthorized",
                "Invalid email or password",
                request.getRequestURI( )
        );

        return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).body( error );
    }

    @ExceptionHandler( AccessDeniedException.class )
    public ResponseEntity< ErrorResponse > handleAccessDenied( AccessDeniedException ex, HttpServletRequest request ) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.FORBIDDEN.value( ),
                "Forbidden",
                "You do not have permission to perform this action",
                request.getRequestURI( )
        );

        return ResponseEntity.status( HttpStatus.FORBIDDEN ).body( error );
    }

    @ExceptionHandler( NonUniqueResultException.class )
    public ResponseEntity<ErrorResponse> handleNonUniqueResult( NonUniqueResultException ex, HttpServletRequest request ) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value( ),
                "Internal server error",
                "A data consistency issue occurred. Please contact support",
                request.getRequestURI( )
        );

        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ). body( error );
    }

    @ExceptionHandler( ResourceNotFoundException.class )
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException( ResourceNotFoundException ex, HttpServletRequest request ) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value( ),
                "Not found",
                ex.getMessage( ),
                request.getRequestURI( )
        );

        return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( error );
    }

    @ExceptionHandler( Exception.class )
    public ResponseEntity<ErrorResponse> handleGeneric( Exception ex, HttpServletRequest request ) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value( ),
                "Internal Server Error",
                "An unexpected error occurred",
                request.getRequestURI( )
        );

        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).body( error );
    }

}

