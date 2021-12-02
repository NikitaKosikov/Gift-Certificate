package com.epam.esm.validator;

import com.epam.esm.dto.impl.GiftCertificateDto;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.util.MessageKey;
import com.epam.esm.util.ValidValue;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class provides methods to validate fields of {@link GiftCertificateDto}.
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Component
public class GiftCertificateValidator {

    private static final Logger logger = LogManager.getLogger();
    private final Map<String, String> incorrectParameters = new LinkedHashMap<>();

    /**
     * Validate all fields of gift certificate dto
     *
     * @param giftCertificateDto the gift certificate as dto for validation
     * @throws IncorrectParameterValueException in case incorrect name, description,
     *                                          price or duration
     */
    public void validate(GiftCertificateDto giftCertificateDto) throws IncorrectParameterValueException{
        validateName(giftCertificateDto.getName());
        validateDescription(giftCertificateDto.getDescription());
        validatePrice(giftCertificateDto.getPrice());
        validateDuration(giftCertificateDto.getDuration());

        if (!incorrectParameters.isEmpty()) {
            throw new IncorrectParameterValueException("error in validating the parameters of the gift certificate",
                    incorrectParameters, ErrorCode.GIFT_CERTIFICATE.getCode());
        }
    }

    private void validateName(String name){
        if (StringUtils.isBlank(name) || name.length() > ValidValue.MAX_LENGTH_NAME){
            incorrectParameters.put(MessageKey.PARAMETER_NAME, name);
            logger.error("gift certificate name error");
        }
    }

    private void validateDescription(String description){
        if (StringUtils.isBlank(description) || description.length() > ValidValue.MAX_LENGTH_DESCRIPTION){
            incorrectParameters.put(MessageKey.PARAMETER_DESCRIPTION, description);
            logger.error("gift certificate description error");
        }
    }

    private void validatePrice(BigDecimal price){
        if (price==null || price.scale() != ValidValue.REQUIRE_SCALE || price.compareTo(ValidValue.MIN_PRICE) < 0
                || price.compareTo(ValidValue.MAX_PRICE) > 0){
            incorrectParameters.put(MessageKey.PARAMETER_PRICE, price == null ? null : price.toString());
            logger.error("gift certificate price error");
        }
    }

    private void validateDuration(Integer duration){
        if (duration == null || duration < ValidValue.MIN_DURATION || duration > ValidValue.MAX_DURATION) {
            incorrectParameters.put(MessageKey.PARAMETER_DURATION, String.valueOf(duration));
            logger.error("gift certificate duration error");
        }
    }

    /**
     * Validate gift certificate id
     *
     * @param id the gift certificate id for validation
     * @throws IncorrectParameterValueException in case incorrect id
     */
    public void validateId(Long id) throws IncorrectParameterValueException {
        if (id < ValidValue.MIN_ID) {
            Map<String, String> incorrectParameter = new HashMap<>();
            incorrectParameter.put(MessageKey.PARAMETER_ID, String.valueOf(id));
            logger.error("id error");
            throw new IncorrectParameterValueException("id validation error", incorrectParameter,
                    ErrorCode.GIFT_CERTIFICATE.getCode());
        }
    }
    
}
