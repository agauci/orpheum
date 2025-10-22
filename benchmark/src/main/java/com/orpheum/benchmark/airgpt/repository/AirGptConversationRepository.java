package com.orpheum.benchmark.airgpt.repository;

import com.orpheum.benchmark.airgpt.model.AirGptConversation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for managing AirGptConversation entities.
 */
public interface AirGptConversationRepository extends CrudRepository<AirGptConversation, UUID> {

}
