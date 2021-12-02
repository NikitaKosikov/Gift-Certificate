package com.epam.esm.exception;

import java.io.Serial;
import java.util.Collections;
import java.util.Map;

/**
 * Exception is generated in case received parameters have unacceptable value
 *
 * @author Nikita Kosikov
 * @version 1.0
 * @see RuntimeException
 */
public class IncorrectParameterValueException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private Map<String, String> parameters;
    private String errorCode;

    public IncorrectParameterValueException() {
        super();
    }

    public IncorrectParameterValueException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Incorrect parameter value exception
     *
     * @param message the error message
     * @param parameters the Map with parameter name key and parameter value
     * @param errorCode the code for building error code in a message to the client
     */
    public IncorrectParameterValueException(String message, Map<String, String> parameters, String errorCode) {
        super(message);
        this.parameters = parameters;
        this.errorCode = errorCode;
    }

    /**
     * Instantiates a new Incorrect parameter value exception
     *
     * @param message the error message
     * @param parameters the Map with parameter name key and parameter value
     * @param errorCode the code for building error code in a message to the client
     */
    public IncorrectParameterValueException(String message, Map<String, String> parameters, String errorCode,
                                            Exception exception) {
        super(message, exception);
        this.parameters = parameters;
        this.errorCode = errorCode;
    }

    /**
     * Get parameters
     *
     * @return Map with parameter name key and parameter value
     */
    public Map<String, String> getParameters() {
        return parameters == null ? Collections.emptyMap() : Collections.unmodifiableMap(parameters);
    }

    /**
     * Get error code
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}
