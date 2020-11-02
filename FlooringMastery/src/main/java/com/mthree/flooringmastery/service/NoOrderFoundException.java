/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.service;

/**
 * exception thrown when order is not found
 * @author beshoy
 */
public class NoOrderFoundException extends Exception {

    public NoOrderFoundException(String message) {
        super(message);
    }

    public NoOrderFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
