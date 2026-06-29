package com.orpheum.benchmark.config.db.converters;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
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

        return objectMapper.readTree(source.getValue());
    }
}
