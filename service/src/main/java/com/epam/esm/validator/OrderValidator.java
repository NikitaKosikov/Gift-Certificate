package com.epam.esm.validator;

import com.epam.esm.dto.impl.OrderDto;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.util.MessageKey;
import com.epam.esm.util.ValidValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class provides methods to validate fields of {@link OrderDto}.
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Component
public class OrderValidator {

    private static final Logger logger = LogManager.getLogger();
    private final Map<String, String> incorrectParameters = new LinkedHashMap<>();

    /**
     * Validate all fields of order as dto.
     *
     * @param orderDto the order as dto for validation
     * @throws IncorrectParameterValueException in case incorrect parameters
     */
    public void validate(OrderDto orderDto) throws IncorrectParameterValueException{

        validateHasGiftCertificateId(orderDto.getGiftCertificateId());
        validateHasUserId(orderDto.getUserId());
        validatePrice(orderDto.getPrice());

        if (!incorrectParameters.isEmpty()) {
            throw new IncorrectParameterValueException("error in validating the parameters of the order",
                    incorrectParameters, ErrorCode.ORDER.getCode());
        }
    }

    private void validateHasGiftCertificateId(Long giftCertificateId) {
        if (giftCertificateId==null){
            incorrectParameters.put(MessageKey.PARAMETER_GIFT_CERTIFICATE, null);
            logger.error("order hasn't gift certificate error");
        }
    }

    private void validateHasUserId(Long userId) {
        if (userId==null){
            incorrectParameters.put(MessageKey.PARAMETER_USER, null);
            logger.error("order hasn't user error");
        }
    }

    private void validatePrice(BigDecimal price){
        if (price==null || price.scale() != ValidValue.REQUIRE_SCALE || price.compareTo(ValidValue.MIN_PRICE) < 0
                || price.compareTo(ValidValue.MAX_PRICE) > 0){
            incorrectParameters.put(MessageKey.PARAMETER_PRICE, price == null ? null : price.toString());
            logger.error("gift certificate price error");
        }
    }

    /**
     * Validate order id
     *
     * @param id the order id for validation
     * @throws IncorrectParameterValueException in case incorrect id
     */
    public void validateId(Long id) throws IncorrectParameterValueException {
        if (id < ValidValue.MIN_ID) {
            Map<String, String> incorrectParameter = new HashMap<>();
            incorrectParameter.put(MessageKey.PARAMETER_ID, String.valueOf(id));
            logger.error("id error");
            throw new IncorrectParameterValueException("id validation error", incorrectParameter,
                    ErrorCode.ORDER.getCode());
        }
    }
}
