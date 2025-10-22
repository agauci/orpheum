package com.orpheum.benchmark.config.objectmapper.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.ai.chat.messages.ToolResponseMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ToolResponseMessageDeserializer extends StdDeserializer<ToolResponseMessage> {
    public ToolResponseMessageDeserializer() {
        super(ToolResponseMessage.class);
    }

    @Override
    public ToolResponseMessage deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p.getValueAsString());

        // --- Extract metadata ---
        JsonNode metadataNode = node.get("metadata");
        Map<String, Object> metadata = metadataNode != null
                ? mapper.convertValue(metadataNode, Map.class)
                : Map.of();

        // --- Extract responses ---
        List<ToolResponseMessage.ToolResponse> responses = new ArrayList<>();
        JsonNode responsesNode = node.get("responses");
        if (responsesNode != null && responsesNode.isArray()) {
            for (JsonNode responseNode : responsesNode) {
                String id = responseNode.has("id") ? responseNode.get("id").asText() : null;
                String name = responseNode.has("name") ? responseNode.get("name").asText() : null;
                String responseData = responseNode.has("responseData") ? responseNode.get("responseData").asText() : null;

                responses.add(new ToolResponseMessage.ToolResponse(id, name, responseData));
            }
        }
        return new ToolResponseMessage(responses, metadata);
    }
}