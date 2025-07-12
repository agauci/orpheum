package com.orpheum.orchestrator.backstage.portal.controller;

import com.orpheum.orchestrator.backstage.portal.exception.AuthTokenNotFoundException;
import com.orpheum.orchestrator.backstage.portal.model.auth.BackstageAuthorisationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(AuthTokenNotFoundException.class)
    public ResponseEntity<List<BackstageAuthorisationRequest>> handleAuthTokenNotFoundException(AuthTokenNotFoundException e) {
        log.error("Received unauthorized request. [Exception: {}, AuthToken: {}]", e.getMessage(), e.getAuthToken());

        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

}
