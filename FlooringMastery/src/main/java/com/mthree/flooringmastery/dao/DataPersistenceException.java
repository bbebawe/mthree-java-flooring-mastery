/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.dao;

/**
 * exception thrown when data can not be read or write from text files
 * @author beshoy
 */
public class DataPersistenceException extends Exception {

    public DataPersistenceException(String message) {
        super(message);
    }

    public DataPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
