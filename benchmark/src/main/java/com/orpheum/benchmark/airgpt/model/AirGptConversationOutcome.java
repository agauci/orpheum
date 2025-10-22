package com.orpheum.benchmark.airgpt.model;

import java.util.UUID;

public record AirGptConversationOutcome(
    UUID conversationId,
    AirGptLlmOutcome llmOutcome
) { }
