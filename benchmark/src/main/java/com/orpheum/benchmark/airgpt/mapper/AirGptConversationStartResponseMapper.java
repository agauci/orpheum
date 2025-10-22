package com.orpheum.benchmark.airgpt.mapper;

import com.orpheum.benchmark.airgpt.model.AirGptConversationOutcome;
import com.orpheum.benchmark.model.AirGptConversationStartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AirGptConversationStartResponseMapper {

    @Mapping(target = "assistantResponse", source = "llmOutcome.assistantMessage")
    @Mapping(target = "conversationTitle", source = "llmOutcome.conversationTitle")
    AirGptConversationStartResponse map(AirGptConversationOutcome outcome);

}
