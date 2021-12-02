package com.epam.esm.dto.impl.deserializer;

import java.io.IOException;

import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.util.MessageKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Class converts String to Integer
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
public class NumberDeserializer extends JsonDeserializer<Integer> {

    private static final String NUMBER_PATTERN = "^\\d{1,9}";

    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String duration = p.getValueAsString();
        if (duration == null || !duration.matches(NUMBER_PATTERN)) {
            throw new IncorrectParameterValueException(MessageKey.TYPE_NUMBER);
        }
        return Integer.valueOf(duration);
    }
}