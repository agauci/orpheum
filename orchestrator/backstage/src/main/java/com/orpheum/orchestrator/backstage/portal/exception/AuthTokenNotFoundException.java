package com.orpheum.orchestrator.backstage.portal.exception;

import lombok.Data;

@Data
public class AuthTokenNotFoundException extends RuntimeException {
    private final String authToken;
}
