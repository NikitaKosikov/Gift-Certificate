package com.epam.esm.exception;

import java.io.Serial;

/**
 * Exception is generated in case resource entities don't found
 * in database
 *
 * @author Nikita Kosikov
 * @version 1.0
 * @see RuntimeException
 */
public class CustomResourceNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1L;

    private final String messageKey;
    private final String messageParameter;
    private final String errorCode;

    /**
     * Instantiates a new Resource not found exception
     *
     * @param message the error message
     * @param messageKey the message key to get message from properties files
     * @param messageParameter the message parameter to set into message from
     *                         properties files
     * @param errorCode the code for building error code in a message to the
     *                  client
     */
    public CustomResourceNotFoundException(String message, String messageKey, String messageParameter, String errorCode) {
        super(message);
        this.messageKey = messageKey;
        this.messageParameter = messageParameter;
        this.errorCode = errorCode;
    }

    /**
     * Get message key
     *
     * @return the message key
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * Get message parameter
     *
     * @return the message parameter
     */
    public String getMessageParameter() {
        return messageParameter;
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
