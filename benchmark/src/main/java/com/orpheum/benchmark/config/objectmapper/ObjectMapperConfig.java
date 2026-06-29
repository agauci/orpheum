package com.orpheum.benchmark.config.objectmapper;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.module.SimpleModule;
import com.orpheum.benchmark.config.objectmapper.deserializers.ToolResponseMessageDeserializer;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class ObjectMapperConfig {

    @Bean
    public SimpleModule toolResponseMessageModule() {
        SimpleModule module = new SimpleModule();

        module.addDeserializer(
                ToolResponseMessage.class,
                new ToolResponseMessageDeserializer()
        );

        return module;
    }

    @Bean
    public JsonMapperBuilderCustomizer jsonMapperBuilderCustomizer() {
        return builder -> builder
                .enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

}
