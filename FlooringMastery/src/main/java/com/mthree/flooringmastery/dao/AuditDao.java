/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.dao;

/**
 * collection of methods to handle audit entries
 *
 * @author beshoy
 */
public interface AuditDao {

    /**
     * writes entry to log file
     *
     * @param entry entry to write to file
     * @throws DataPersistenceException
     */
    public void writeAuditEntry(String entry) throws DataPersistenceException;

}
