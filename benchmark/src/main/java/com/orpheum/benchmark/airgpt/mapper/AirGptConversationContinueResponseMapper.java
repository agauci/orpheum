package com.orpheum.benchmark.airgpt.mapper;

import com.orpheum.benchmark.airgpt.model.AirGptConversationOutcome;
import com.orpheum.benchmark.model.AirGptConversationContinueResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AirGptConversationContinueResponseMapper {

    @Mapping(target = "assistantResponse", source = "llmOutcome.assistantMessage")
    AirGptConversationContinueResponse map(AirGptConversationOutcome outcome);

}
