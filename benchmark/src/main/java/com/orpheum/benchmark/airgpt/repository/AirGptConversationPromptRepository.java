package com.orpheum.benchmark.airgpt.repository;

import com.orpheum.benchmark.airgpt.model.AirGptConversationPrompt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for managing AirGptConversationPrompt entities.
 */
public interface AirGptConversationPromptRepository extends CrudRepository<AirGptConversationPrompt, UUID> {

    /**
     * Find all prompts for a specific conversation.
     *
     * @param conversationId the conversation ID
     * @return a list of prompts for the conversation
     */
    List<AirGptConversationPrompt> findByConversationIdOrderByTimestampAsc(UUID conversationId);

    void deleteByConversationId(UUID conversationId);

    AirGptConversationPrompt findFirstByConversationIdOrderByTimestampDesc(UUID conversationId);
}
