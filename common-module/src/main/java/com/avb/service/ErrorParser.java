package com.avb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import com.avb.model.AVBError;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ErrorParser {
    private final ObjectMapper objectMapper;

    public AVBError parseError(String json) {
        try {
            return objectMapper.readValue(json, AVBError.class);
        } catch (Exception e) {
            return new AVBError("500", "Error parsing error response");
        }
    }
}