package com.orpheum.benchmark.config.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orpheum.benchmark.config.db.converters.JsonNodeToPgObjectConverter;
import com.orpheum.benchmark.config.db.converters.PgObjectToJsonNodeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.util.List;

@Configuration
@EnableJdbcAuditing
@EnableJdbcRepositories(basePackages = "com.orpheum.benchmark")
public class JdbcConfig extends AbstractJdbcConfiguration {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected List<?> userConverters() {
        return List.of(
                new JsonNodeToPgObjectConverter(objectMapper),
                new PgObjectToJsonNodeConverter(objectMapper)
        );
    }

}
