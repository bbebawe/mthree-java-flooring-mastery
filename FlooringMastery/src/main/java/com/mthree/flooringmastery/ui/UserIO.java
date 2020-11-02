/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * collection of methods used to handle user inputs
 *
 * @author beshoy
 */
public interface UserIO {

    /**
     * prints message to user
     *
     * @param message String to be print
     */
    public void print(String message);

    /**
     * prompts user for string input
     *
     * @param prompt prompt message
     * @return String input
     */
    public String readString(String prompt);

    /**
     * prompts user for string input 
     *
     * @param prompt prompt message
     * @param canBeBlank boolean value to allow blank input
     * @return String input
     */
    public String readString(String prompt, boolean canBeBlank);

    /**
     * prompt user to enter a string that falls within the accepted values
     *
     * @param prompt prompt message
     * @param allowedValues list of allowed products
     * @param canBeBlank boolean value to allow method to accept blank input
     * @return String value
     */
    public String readString(String prompt, List<String> allowedValues, boolean canBeBlank);

    /**
     * prompts message to user and reads integer
     *
     * @param prompt String message
     * @return Integer - number from user input
     */
    int readInt(String prompt);

    /**
     * prompts user to input integer within the minimum value
     *
     * @param prompt prompt message
     * @param min minimum value allowed
     * @return Integer within minimum value
     */
    int readInt(String prompt, int min);

    /**
     * prompts message to user and reads Integer and read number between min and
     * max value
     *
     * @param prompt String message
     * @param min Integer - min number allowed
     * @param max Integer - max number allowed
     * @return Integer - number from user input between min and max values
     */
    int readInt(String prompt, int min, int max);

    /**
     * prompt user to enter BigDecimal value
     *
     * @param prompt prompt message
     * @return BigDecimal object
     */
    public BigDecimal readBigDecimal(String prompt);

    /**
     * prompts user to enter a BigDecimal that is >= minimum value
     *
     * @param prompt prompt message
     * @param minimumValue minimum allowed value
     * @param canBeBlank boolean value to allow method to accept blank input
     * @return BigDecimal value
     */
    public BigDecimal readBigDecimal(String prompt, BigDecimal minimumValue, boolean canBeBlank);

    /**
     * prompts user to enter date
     *
     * @param prompt prompt message
     * @return LocalDate object
     */
    public LocalDate readLocalDate(String prompt);

    /**
     * prompts user to enter a date that is >= the minimum date allowed
     *
     * @param prompt prompt message
     * @param minDateAllowed minimum allowed date
     * @return LocalDate object
     */
    public LocalDate readLocalDate(String prompt, LocalDate minDateAllowed);

    /**
     * prompt user for next operation prompt (y/n)
     *
     * @param prompt operation prompt
     * @return boolean value corresponds to yer or no
     */
    public boolean readNextOperation(String prompt);

    /**
     * prompts user to hit enter to continue
     *
     * @param prompt prompt
     * @return String - blank input
     */
    String readEnter(String prompt);
}
