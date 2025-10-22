package com.orpheum.benchmark.config.db.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.sql.SQLException;

@WritingConverter
public class JsonNodeToPgObjectConverter implements Converter<JsonNode, PGobject> {

    private final ObjectMapper objectMapper;

    public JsonNodeToPgObjectConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public PGobject convert(JsonNode source) {
        PGobject pgObject = new PGobject();
        pgObject.setType("json");

        try {
            if (source == null || source.isNull()) {
                pgObject.setValue(null);
            } else {
                // Convert JsonNode directly to JSON string
                pgObject.setValue(objectMapper.writeValueAsString(source));
            }
            return pgObject;
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JsonNode to PGobject", e);
        }
    }
}
