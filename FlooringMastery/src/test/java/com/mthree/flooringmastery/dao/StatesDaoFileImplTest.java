/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.dto.State;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author beshoy
 */
@DisplayName(" States Dao Implementation Test")
public class StatesDaoFileImplTest {

    private StatesDao statesDao;

    public StatesDaoFileImplTest() {
        ApplicationContext appContext
                = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        statesDao = appContext.getBean("statesDao", StatesDaoFileImpl.class);
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    @DisplayName("Test states dao to load all states from file")
    public void testStatesDao_toLoadAllStatesFromFile() throws DataPersistenceException {
        // load data from test file
        statesDao.loadStates();
        // assert
        assertAll(
                () -> assertDoesNotThrow(() -> statesDao.loadStates(), "should not throw exception because file exsit"),
                () -> assertFalse(statesDao.getAllStates().isEmpty(), "should not be empty because file has 4 states"),
                () -> assertEquals(4, statesDao.getAllStates().size(), "should be 4, because test file has 4 states")
        );
    }

    @Test
    @DisplayName("Test states dao to unmarshall data correctly into State object")
    public void testStatesDao_toUnmarshallDataIntoStateObject() throws DataPersistenceException {
        // setup test data
        State testState = new State("TX", "Texas", new BigDecimal("4.45"));
        // load data from test file
        statesDao.loadStates();
        assertAll(
                () -> assertDoesNotThrow(() -> statesDao.getState(testState.getStateAbbreviation()), "should not throw exception because Texas states is in file"),
                () -> assertEquals(testState, statesDao.getState(testState.getStateAbbreviation()), "should  be equal to Texas, TX, 4.45"),
                () -> assertEquals("Texas", statesDao.getState(testState.getStateAbbreviation()).getStateName(), "should be Texas"),
                () -> assertEquals("TX", statesDao.getState(testState.getStateAbbreviation()).getStateAbbreviation(), "should be TX"),
                () -> assertEquals(new BigDecimal("4.45"), statesDao.getState(testState.getStateAbbreviation()).getTaxRate(), "should be 4.45")
        );
    }

    @Test
    @DisplayName("Test states dao to return list of all states")
    public void testStatesDao_toReturnListOfStates() throws DataPersistenceException {
        // set up test data
        List<State> testStates = new ArrayList<>();
        testStates.add(new State("TX", "Texas", new BigDecimal("4.45")));
        testStates.add(new State("WA", "Washington", new BigDecimal("9.25")));
        testStates.add(new State("KY", "Kentucky", new BigDecimal("6.00")));
        testStates.add(new State("CA", "Calfornia", new BigDecimal("25.00")));
        // load data from test file
        statesDao.loadStates();
        assertAll(
                () -> assertEquals(testStates, statesDao.getAllStates(), " should be equals becase test list have same states in test file"),
                () -> assertTrue(statesDao.getAllStates().contains(testStates.get(0)), "should be true"),
                () -> assertTrue(statesDao.getAllStates().contains(testStates.get(3)), "should be true")
        );
    }

    @Test
    @DisplayName("Test states dao to retrun list of all states codes")
    public void testStatesDao_toReturnCodesList() throws DataPersistenceException {
        // set test data
        List<String> testCodes = new ArrayList<>();
        testCodes.add("TX");
        testCodes.add("WA");
        testCodes.add("KY");
        testCodes.add("CA");
        // load test data from file
        statesDao.loadStates();
        assertAll(
                () -> assertFalse(statesDao.getAllStatesCodes().isEmpty(), " should not be empty, has 4 items"),
                () -> assertEquals(testCodes.size(), statesDao.getAllStatesCodes().size(), "should be 4"),
                () -> assertEquals(testCodes, statesDao.getAllStatesCodes(), "should be equal")
        );
    }

    @Test
    @DisplayName(" Test states dao to return null when state does not exist")
    public void testStatesDao_toRetunNullWhenNoState() throws DataPersistenceException {
        // load test data from file
        statesDao.loadStates();
        assertNull(statesDao.getState("NY"), "New York is not in the stats list, should be null");
        assertNull(statesDao.getState("OH"), "Ohio is not in the stats list, should be null");   
    }
}
