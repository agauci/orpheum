package com.orpheum.benchmark.airgpt.mapper;

import com.orpheum.benchmark.model.PricingStrategyMode;
import com.orpheum.benchmark.model.StartAirGptConversationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PricingStrategyMapper {

    PricingStrategyMode map(StartAirGptConversationRequest.PricingStrategyEnum pricingStrategyEnum);

}
