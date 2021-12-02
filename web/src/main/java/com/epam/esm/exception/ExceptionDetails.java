package com.epam.esm.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Class presents entity which will be returned from controller in case
 * generating some exception
 *
 * @author Nikita Kosikov
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public class ExceptionDetails {

    private final String errorMessage;
    private final String errorCode;
}
