package com.moro.tchat.controller;

import com.moro.tchat.dto.response.ErrorResponse;
import com.moro.tchat.exception.EntityExistException;
import com.moro.tchat.exception.EntityNotFoundException;
import com.moro.tchat.exception.VerificationException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({JwtException.class})
    public ResponseEntity<ErrorResponse> jwtException(JwtException e) {
        ErrorResponse response = ErrorResponse.builder()
                .httpStatus(HttpStatus.FORBIDDEN.value())
                .date(LocalDateTime.now())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(response);
    }

    @ExceptionHandler({NoResourceFoundException.class})
    public ResponseEntity<ErrorResponse> endpointNotExistException(NoResourceFoundException e) {
        ErrorResponse response = ErrorResponse.builder()
                .httpStatus(HttpStatus.NOT_FOUND.value())
                .date(LocalDateTime.now())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(response);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorResponse> authenticationException(AuthenticationException e) {
        ErrorResponse response = ErrorResponse.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED.value())
                .date(LocalDateTime.now())
                .message("Authentication Error")
                .details(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(response);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> entityNotFoundException(EntityNotFoundException e) {
        ErrorResponse response = ErrorResponse.builder()
                .httpStatus(e.getHttpStatus().value())
                .date(LocalDateTime.now())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(e.getHttpStatus().value()).body(response);
    }

    @ExceptionHandler({EntityExistException.class})
    public ResponseEntity<ErrorResponse> entityExistException(EntityExistException e) {
        ErrorResponse response = ErrorResponse.builder()
                .httpStatus(e.getHttpStatus().value())
                .date(LocalDateTime.now())
                .message(e.getMessage())
                .errors(e.getErrors())
                .build();

        return ResponseEntity.status(e.getHttpStatus().value()).body(response);
    }

    @ExceptionHandler({VerificationException.class})
    public ModelAndView verificationException(VerificationException e) {
        ModelAndView modelAndView = new ModelAndView("email-verification-unsuccessful");
        modelAndView.addObject("error", e.getMessage());

        return modelAndView;
    }

}
