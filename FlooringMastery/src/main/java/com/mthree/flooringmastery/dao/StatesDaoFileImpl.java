/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.dto.State;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * implements StatesDao interface using text files as data source
 *
 * @author beshoy
 */
public class StatesDaoFileImpl implements StatesDao {

    private Map<String, State> states;
    private String statesFile;
    private String delimiter;

    public StatesDaoFileImpl() {
    }

    public StatesDaoFileImpl(Map<String, State> states, String statesFile, String delimiter) {
        this.states = states;
        this.statesFile = statesFile;
        this.delimiter = delimiter;
    }

    @Override
    public void loadStates() throws DataPersistenceException {
        Scanner scanner;
        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(statesFile)));
        } catch (FileNotFoundException e) {
            throw new DataPersistenceException(
                    "Could not load states data into memory.", e);
        }
        // currentLine holds the most recent line read from the file
        String currentLine;
        // SKIP DATA HEADER IN FILE
        scanner.nextLine();
        // holds the most recent state unmarshalled
        State currentState;
        // Process while we have more lines in the file
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into a state 
            currentState = unmarshallState(currentLine);
            // Put state into the map using state abbreviation as key
            states.put(currentState.getStateAbbreviation(), currentState);
        }
        // close scanner
        scanner.close();
    }

    @Override
    public void saveStates() throws DataPersistenceException {
        PrintWriter out;
        try {
            File file = new File(statesFile);
            out = new PrintWriter(file);
        } catch (IOException e) {
            throw new DataPersistenceException(
                    "Could not save states data.", e);
        }
        // create list of states objects to be wrriten to the file
        List<State> statesList = new ArrayList(states.values());
        // write each object in the list
        statesList.stream().forEach((state) -> {
            String stateAsText = marshallState(state);
            out.println(stateAsText);
            out.flush();
        });
        // Clean up
        out.close();
    }

    @Override
    public State getState(String stateAbbreviation) throws DataPersistenceException {
        // returns null if no mapping
        return states.get(stateAbbreviation);
    }

    @Override
    public List<State> getAllStates() throws DataPersistenceException {
        return new ArrayList<State>(states.values());
    }

    @Override
    public List<String> getAllStatesCodes() throws DataPersistenceException {
        // return list of all states codes (keyset) 
        return new ArrayList<String>(states.keySet());
    }

    /**
     * creates state object from string that contains marshalled state object
     *
     * @param stateAsText marshalled object string
     * @return State object
     */
    private State unmarshallState(String stateAsText) {
        String[] stateTokens = stateAsText.split(delimiter);
        State stateFromFile = new State();
        // index 0 - abbreviation
        stateFromFile.setStateAbbreviation(stateTokens[0]);
        // index 1 - state name
        stateFromFile.setStateName(stateTokens[1]);
        // index 2 - tax rate
        stateFromFile.setTaxRate(new BigDecimal(stateTokens[2]));
        // return created state object
        return stateFromFile;
    }

    /**
     * creates a string representation of State object
     *
     * @param state State object
     * @return state object as string
     */
    private String marshallState(State state) {
        // create String to represnt state object
        String stateAsText = state.getStateAbbreviation() + delimiter;
        stateAsText += state.getStateName() + delimiter;
        stateAsText += state.getTaxRate(); // no delimiter for last property
        // state object as string
        return stateAsText;
    }
}
