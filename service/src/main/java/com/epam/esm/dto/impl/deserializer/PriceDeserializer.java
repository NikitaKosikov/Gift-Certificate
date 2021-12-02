package com.epam.esm.dto.impl.deserializer;

import java.io.IOException;
import java.math.BigDecimal;

import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.util.MessageKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Class converts String to BigDecimal
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
public class PriceDeserializer extends JsonDeserializer<BigDecimal> {

    private static final String PRICE_PATTERN = "^\\d{1,8}(\\.\\d{1,2})?$";

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String price = p.getValueAsString();
        if (price == null || !price.matches(PRICE_PATTERN)) {
            throw new IncorrectParameterValueException(MessageKey.TYPE_PRICE);
        }
        return new BigDecimal(price);
    }

}