package com.orpheum.benchmark.airgpt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orpheum.benchmark.airgpt.model.AirGptConversation;
import com.orpheum.benchmark.airgpt.model.AirGptConversationOutcome;
import com.orpheum.benchmark.airgpt.model.AirGptConversationPrompt;
import com.orpheum.benchmark.airgpt.model.AirGptLlmOutcome;
import com.orpheum.benchmark.airgpt.repository.AirGptConversationPromptRepository;
import com.orpheum.benchmark.airgpt.repository.AirGptConversationRepository;
import com.orpheum.benchmark.airgpt.service.tools.AirGptTools;
import com.orpheum.benchmark.competitor.support.UUIDs;
import com.orpheum.benchmark.model.PricingStrategyMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirGptService {

    private static final BeanOutputConverter<AirGptLlmOutcome> OUTPUT_CONVERTER = new BeanOutputConverter<>(AirGptLlmOutcome.class);

    private final ChatClient chatClient;
    private final AirGptConversationRepository conversationRepository;
    private final AirGptConversationPromptRepository promptRepository;
    private final AirGptChatMemory memory;
    private final AirGptTools tools;

    @Value("classpath:/prompts/airgpt.prompt")
    private Resource airGptPrompt;

    @Transactional
    public AirGptConversationOutcome startConversation(String userPrompt, String internalGroupId, PricingStrategyMode pricingStrategyMode) {
        UUID conversationId = UUIDs.create();

        return processPrompt(conversationId, userPrompt, internalGroupId, pricingStrategyMode, true);
    }

    @Transactional
    public AirGptConversationOutcome continueConversation(UUID conversationId, String userPrompt) {
        Optional<AirGptConversation> conversation = conversationRepository.findById(conversationId);
        if (conversation.isEmpty()) {
            throw new RuntimeException("Unable to find conversation with provided ID " + conversationId);
        }

        return processPrompt(conversationId, userPrompt, conversation.get().getInternalGroupId(), conversation.get().getPricingStrategy(), false);
    }

    private AirGptConversationOutcome processPrompt(UUID conversationId, String userPrompt, String internalGroupId, PricingStrategyMode pricingStrategyMode, boolean isNewConversation) {
        //verifyPromptLimits(subscriptionLevel, userId);

        final String text = generateAirGptPromptText(isNewConversation, internalGroupId, conversationId, pricingStrategyMode);
        final OpenAiChatOptions chatOptions = buildChatOptions();
        final Long startTime = System.currentTimeMillis();

        // Get response from LLM
        final AirGptLlmOutcome extractionOutcome = chatClient.prompt()
                .system(s -> s.text(text))
                .user(u -> u.text(userPrompt))
                .options(chatOptions)
                .advisors(spec ->
                        spec.advisors(
                                MessageChatMemoryAdvisor.builder(memory).build(),
                                SimpleLoggerAdvisor.builder().order(100).build()
                        ).params(Map.of(ChatMemory.CONVERSATION_ID, conversationId.toString()))
                )
                .tools(tools)
                .call()
                .entity(AirGptLlmOutcome.class);

        if (isNewConversation) {
            conversationRepository.save(AirGptConversation.create(conversationId, extractionOutcome.conversationTitle(), internalGroupId, pricingStrategyMode).markAsNew());
        }

        decoratePromptWithLlmOutcome(conversationId, extractionOutcome, System.currentTimeMillis() - startTime, chatOptions);

        return new AirGptConversationOutcome(conversationId, extractionOutcome);
    }

    private String generateAirGptPromptText(boolean isNewConversation, String internalGroupId, UUID conversationId, PricingStrategyMode pricingStrategy) {
        return PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .resource(airGptPrompt)
                .build()
                .render(Map.of(
                        "is_new_conversation", (isNewConversation) ? "yes" : "no",
                        "conversation_id", conversationId.toString(),
                        "internal_group_id", internalGroupId,
                        "pricing_strategy", pricingStrategy.getFullDescription()
                ));
    }

    private OpenAiChatOptions buildChatOptions() {
        return OpenAiChatOptions.builder()
                .model("gpt-5")
                .responseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, OUTPUT_CONVERTER.getJsonSchema()))
                .reasoningEffort("high")
                .temperature(1.0)
                .build();
    }

    private void decoratePromptWithLlmOutcome(UUID conversationId, AirGptLlmOutcome outcome, Long promptDuration, OpenAiChatOptions chatOptions) {
        AirGptConversationPrompt assistantPrompt = promptRepository.findFirstByConversationIdOrderByTimestampDesc(conversationId);
        promptRepository.save(assistantPrompt
                .withPromptDuration(promptDuration)
                .withAssistantMode(outcome.assistantMode())
                .withAssistantContext(outcome.assistantContext())
                .withGptModel(chatOptions.getModel() + " (" + chatOptions.getReasoningEffort() + ")")
        );
    }

}
