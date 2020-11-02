/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.dto.State;
import java.util.List;

/**
 * collection of methods to read and manipulate States data
 *
 * @author beshoy
 */
public interface StatesDao {

    /**
     * loads all states into memory
     *
     * @throws DataPersistenceException
     */
    public void loadStates() throws DataPersistenceException;

    /**
     * saves all states from memory to external resource
     *
     * @throws DataPersistenceException
     */
    public void saveStates() throws DataPersistenceException;

    /**
     * gets state object using state stateAbbreviation
     *
     * @param stateAbbreviation stateAbbreviation to match it against states
     * list
     * @return states object or null if there is no state in the list
     * @throws DataPersistenceException
     */
    public State getState(String stateAbbreviation) throws DataPersistenceException;

    /**
     * gets list of all available state objects
     *
     * @return list of states objects
     * @throws DataPersistenceException
     */
    public List<State> getAllStates() throws DataPersistenceException;

    /**
     * gets list of all states codes (abbreviations)
     *
     * @return list of string with states codes
     * @throws DataPersistenceException
     */
    public List<String> getAllStatesCodes() throws DataPersistenceException;

}
