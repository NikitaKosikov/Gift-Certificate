package com.epam.esm.validator;

import com.epam.esm.dto.impl.UserDto;
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
 * Class provides methods to validate fields of {@link UserDto}.
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Component
public class UserValidator {

    private static final Logger logger = LogManager.getLogger();
    private static final String USERNAME_PATTERN = "^[a-zA-Zà-ÿÀ-ß-\\s]{1,45}$";
    private static final String PASSWORD_PATTERN = "^[a-zA-Z\\d]{5,15}$";
    private final Map<String, String> incorrectParameters = new LinkedHashMap<>();

    /**
     * Validate all fields of user as dto.
     *
     * @param userDto the user as dto for validation
     * @throws IncorrectParameterValueException in case incorrect parameters
     */
    public void validate(UserDto userDto) throws IncorrectParameterValueException{

        validateUsername(userDto.getUsername());
        validatePassword(userDto.getPassword());

        if (!incorrectParameters.isEmpty()) {
            throw new IncorrectParameterValueException("error in validating the parameters of the user",
                    incorrectParameters, ErrorCode.USER.getCode());
        }
    }

    private void validateUsername(String username){
        if (StringUtils.isBlank(username) || !username.matches(USERNAME_PATTERN)){
            incorrectParameters.put(MessageKey.PARAMETER_USERNAME, username);
            logger.error("gift certificate username error");
        }
    }

    private void validatePassword(String password){
        if (StringUtils.isBlank(password) || !password.matches(PASSWORD_PATTERN)){
            incorrectParameters.put(MessageKey.PARAMETER_PASSWORD, password);
            logger.error("gift certificate password error");
        }
    }

    /**
     * Validate user id
     *
     * @param id the user id for validation
     * @throws IncorrectParameterValueException in case incorrect id
     */
    public void validateId(Long id) throws IncorrectParameterValueException {
        if (id < ValidValue.MIN_ID) {
            Map<String, String> incorrectParameter = new HashMap<>();
            incorrectParameter.put(MessageKey.PARAMETER_ID, String.valueOf(id));
            logger.error("id error");
            throw new IncorrectParameterValueException("id validation error", incorrectParameter,
                    ErrorCode.USER.getCode());
        }
    }
}
