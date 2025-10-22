package com.orpheum.benchmark.config.objectmapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.orpheum.benchmark.config.objectmapper.deserializers.ToolResponseMessageDeserializer;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class ObjectMapperConfig {

    ObjectMapper objectMapper;

    @PostConstruct
    public void configure() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ToolResponseMessage.class, new ToolResponseMessageDeserializer());

        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.registerModule(module);
    }

}
