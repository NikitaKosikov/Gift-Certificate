package com.epam.esm.validator;

import com.epam.esm.dto.impl.TagDto;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.util.MessageKey;
import com.epam.esm.util.ValidValue;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class provides methods to validate fields of {@link TagDto}.
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Component
public class TagValidator {


    private static final Logger logger = LogManager.getLogger();
    private final Map<String, String> incorrectParameters = new LinkedHashMap<>();

    /**
     * Validate all fields of tag as dto.
     *
     * @param tagDto the tag as dto for validation
     * @throws IncorrectParameterValueException in case incorrect parameters
     */
    public void validate(TagDto tagDto) throws IncorrectParameterValueException{

        validateName(tagDto.getName());

        if (!incorrectParameters.isEmpty()) {
            throw new IncorrectParameterValueException("error in validating the parameters of the tag",
                    incorrectParameters, ErrorCode.TAG.getCode());
        }
    }

    private void validateName(String name){
        if (StringUtils.isBlank(name) || name.length() > ValidValue.MAX_LENGTH_NAME){
            incorrectParameters.put(MessageKey.PARAMETER_NAME, name);
            logger.error("gift certificate name error");
        }
    }

    /**
     * Validate tag id
     *
     * @param id the tag id for validation
     * @throws IncorrectParameterValueException in case incorrect id
     */
    public void validateId(Long id) throws IncorrectParameterValueException {
        if (id < ValidValue.MIN_ID) {
            Map<String, String> incorrectParameter = new HashMap<>();
            incorrectParameter.put(MessageKey.PARAMETER_ID, String.valueOf(id));
            logger.error("id error");
            throw new IncorrectParameterValueException("id validation error", incorrectParameter,
                    ErrorCode.TAG.getCode());
        }
    }

}
