package com.orpheum.benchmark.config.db.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;

public class PgObjectToJsonNodeConverter implements Converter<PGobject, JsonNode> {

    private final ObjectMapper objectMapper;

    public PgObjectToJsonNodeConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode convert(PGobject source) {
        if (source == null || source.getValue() == null) {
            return objectMapper.nullNode();
        }

        try {
            return objectMapper.readTree(source.getValue());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert PGobject to JsonNode", e);
        }
    }
}
