package com.techfoot.stockspree.InboundAdaptors.Configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Configuration class for custom deserializers and ObjectMapper setup
 */
@Configuration
public class DeserializerConfig {

    @Autowired
    private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

    /**
     * Configure ObjectMapper with custom error handling and date formatting
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        // Build ObjectMapper using the customized Jackson2ObjectMapperBuilder
        ObjectMapper mapper = jackson2ObjectMapperBuilder.createXmlMapper(false).build();
        
        // Configure to be lenient with unknown properties
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        // Configure to handle missing properties gracefully
        mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
        
        // Configure to handle null values gracefully
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        
        // Configure to handle empty strings as null for primitives
        mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
        
        return mapper;
    }

    /**
     * Register shared custom deserializer
     */
    @Bean
    public SharedCustomDeserializer sharedCustomDeserializer() {
        return new SharedCustomDeserializer();
    }
} 