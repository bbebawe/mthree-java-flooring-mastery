/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.service;

/**
 * 
 * @author beshoy
 */
public class InvalidStateException extends Exception {

    public InvalidStateException(String message) {
        super(message);
    }

    public InvalidStateException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
