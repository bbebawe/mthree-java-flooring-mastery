/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.service;

/**
 * exception thrown when order does not contain new data for editing
 * @author beshoy
 */
public class NoEditDataExceprtion extends Exception {

    public NoEditDataExceprtion(String message) {
        super(message);
    }

    public NoEditDataExceprtion(String message, Throwable cause) {
        super(message, cause);
    }

   
}
