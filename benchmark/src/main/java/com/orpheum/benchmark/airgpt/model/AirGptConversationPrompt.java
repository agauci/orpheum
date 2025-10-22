package com.orpheum.benchmark.airgpt.model;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.databind.JsonNode;
import com.orpheum.benchmark.model.AbstractEntity;
import lombok.Value;
import lombok.With;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Model class representing the airgpt_conversation_prompt table.
 */
@Value
@With
@Table("airgpt_conversation_prompt")
public class AirGptConversationPrompt extends AbstractEntity<UUID, AirGptConversationPrompt> {
    @Id
    UUID id;
    UUID conversationId;
    String content;
    String text;
    JsonNode structuredOutput;
    MessageType type;
    AirGptAssistantMode assistantMode;
    String assistantContext;
    Long promptDuration;
    String gptModel;

    // @CreatedDate and @LastModifiedDate do not support OffsetDateTime
    @CreatedDate
    LocalDateTime timestamp;

    @PersistenceCreator
    public static AirGptConversationPrompt create(UUID id, UUID conversationId, String content, String text, JsonNode structuredOutput, MessageType type) {
        return new AirGptConversationPrompt(id, conversationId, content, text, structuredOutput, type, null, null, null, null, null);
    }

}
