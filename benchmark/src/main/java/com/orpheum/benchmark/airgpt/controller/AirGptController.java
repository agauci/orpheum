package com.orpheum.benchmark.airgpt.controller;

import com.orpheum.benchmark.airgpt.mapper.AirGptConversationContinueResponseMapper;
import com.orpheum.benchmark.airgpt.mapper.AirGptConversationStartResponseMapper;
import com.orpheum.benchmark.airgpt.service.AirGptService;
import com.orpheum.benchmark.api.AirGptApi;
import com.orpheum.benchmark.model.AirGptConversationContinueResponse;
import com.orpheum.benchmark.model.AirGptConversationStartResponse;
import com.orpheum.benchmark.model.ContinueAirGptConversationRequest;
import com.orpheum.benchmark.model.StartAirGptConversationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AirGptController implements AirGptApi {

    private final AirGptService service;
    private final AirGptConversationStartResponseMapper startResponseMapper;
    private final AirGptConversationContinueResponseMapper continueResponseMapper;

    @Override
    public ResponseEntity<AirGptConversationStartResponse> startAirGptConversation(StartAirGptConversationRequest request) {
        return ResponseEntity.ok(startResponseMapper.map(service.startConversation(request.getPrompt(), request.getInternalGroupId())));
    }

    @Override
    public ResponseEntity<AirGptConversationContinueResponse> continueAirGptConversation(UUID conversationId, ContinueAirGptConversationRequest request) {
        return ResponseEntity.ok(continueResponseMapper.map(service.continueConversation(conversationId, request.getPrompt())));
    }

}
