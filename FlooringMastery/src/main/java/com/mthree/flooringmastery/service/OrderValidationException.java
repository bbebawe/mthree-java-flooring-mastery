/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.service;

/**
 * exception thrown when order can not be validated
 * @author beshoy
 */
public class OrderValidationException extends Exception {

    public OrderValidationException(String message) {
        super(message);
    }

    public OrderValidationException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
