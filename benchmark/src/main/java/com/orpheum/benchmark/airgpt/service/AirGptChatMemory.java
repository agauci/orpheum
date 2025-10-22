package com.orpheum.benchmark.airgpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orpheum.benchmark.airgpt.model.AirGptConversationPrompt;
import com.orpheum.benchmark.airgpt.model.AirGptLlmOutcome;
import com.orpheum.benchmark.airgpt.repository.AirGptConversationPromptRepository;
import com.orpheum.benchmark.competitor.support.UUIDs;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.messages.MessageType.*;

@Component
@AllArgsConstructor
public class AirGptChatMemory implements ChatMemory {

    AirGptConversationPromptRepository repository;
    ObjectMapper objectMapper;

    @Override
    public void add(String conversationId, List<Message> messages) {
        repository.saveAll(
            messages.stream()
                .map(message ->
                        {
                            try {
                                return AirGptConversationPrompt.create(
                                        UUIDs.create(),
                                        UUIDs.fromString(conversationId),
                                        objectMapper.writeValueAsString(message),
                                        extractText(message),
                                        extractStructuredOutput(message),
                                        message.getMessageType()
                                ).markAsNew();
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException("Unable to process prompt for conversation " + conversationId);
                            }
                        }
                )
                .toList()
        );
    }

    @Override
    public List<Message> get(String conversationId) {
        return repository.findByConversationIdOrderByTimestampAsc(UUIDs.fromString(conversationId)).stream()
                .map(this::convert)
                // Tool calls are not sent to the LLM. The LLM is instead instructed to keep any inferences in its context
                .filter(message -> !TOOL.equals(message.getMessageType()))
                .toList();
    }

    @Override
    public void clear(String conversationId) {
        repository.deleteByConversationId(UUIDs.fromString(conversationId));
    }

    private Message convert(AirGptConversationPrompt prompt) {
        return switch (prompt.getType()) {
            case USER -> objectMapper.convertValue(prompt.getContent(), UserMessage.class);
            case ASSISTANT -> objectMapper.convertValue(prompt.getContent(), AssistantMessage.class);
            case SYSTEM -> objectMapper.convertValue(prompt.getContent(), SystemMessage.class);
            case TOOL -> objectMapper.convertValue(prompt.getContent(), ToolResponseMessage.class);
        };
    }

    private String extractText(Message message) throws JsonProcessingException {
        return switch (message.getMessageType()) {
            case USER -> message.getText();
            case ASSISTANT -> objectMapper.readValue(message.getText(), AirGptLlmOutcome.class).assistantMessage();
            case SYSTEM -> message.getText();
            case TOOL -> message.getText();
        };
    }

    private JsonNode extractStructuredOutput(Message message) throws JsonProcessingException {
        return switch (message.getMessageType()) {
            case USER -> null;
            case ASSISTANT -> objectMapper.readTree(message.getText());
            case SYSTEM -> null;
            case TOOL -> null;
        };
    }

}
