package com.company.tasks.online.store.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonHelper {
    private final ObjectMapper MAPPER = new ObjectMapper();

    public String getJsonFromPojo(final Object pojo) throws JsonProcessingException {
        return MAPPER.writeValueAsString(pojo);
    }

    public <T> T getPojoFromJson(final String json, final Class<T> type) throws IOException {
        return MAPPER.readValue(json, type);
    }
}
