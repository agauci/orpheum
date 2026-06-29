package com.orpheum.benchmark.config;

import com.orpheum.benchmark.airgpt.service.tools.AirGptTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfiguration {

    @Bean
    ToolCallbackProvider toolCallbackProvider(AirGptTools airGptTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(airGptTools)
                .build();
    }
}
