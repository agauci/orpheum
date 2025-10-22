package com.orpheum.benchmark.airgpt.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AirGptLlmOutcome(
        @JsonProperty(required = true, value = "assistantMessage") String assistantMessage,
        @JsonProperty(required = true, value = "conversationTitle") String conversationTitle,
        @JsonProperty(required = true, value = "assistantMode") AirGptAssistantMode assistantMode,
        @JsonProperty(required = true, value = "assistantContext") String assistantContext
) {
}
