/*
 * @Copyright Beshoy Bebawe 2020.
 */
package com.mthree.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * implements UserIo interface using console
 *
 * @author beshoy
 */
public class UserIOConsoleImpl implements UserIO {

    private Scanner console = new Scanner(System.in);

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        print(prompt);
        return console.nextLine();
    }

    @Override
    public String readString(String prompt, boolean canBeBlank) {
        boolean validInput = false;
        String userInput;
        do {
            print(prompt);
            userInput = console.nextLine();
            if (userInput.isBlank()) {
                print("input can not be blank");
            } else {
                validInput = true;
            }
        } while (!validInput);
        return userInput;
    }

    @Override
    public String readString(String prompt, List<String> allowedValues, boolean canBeBlank) {
        String userInput = null;
        // allows user to ignored cased input, then return value from list so data is consistent
        String valueToReturn = null;
        boolean validInput = false;
        do {
            userInput = readString(prompt);
            // allow blank input if true
            if (canBeBlank && userInput.isBlank()) {
                // return empty string
                valueToReturn = "";
                validInput = true;
            } else {
                // loop throw list and match input
                // this block is added so can user can input ignored case
                for (String value : allowedValues) {
                    if (value.equalsIgnoreCase(userInput)) {
                        valueToReturn = value;
                        validInput = true;
                        break; // if match found break from loop 
                    }
                }
            }
            // display error message if not valid
            if (!validInput) {
                print(userInput + " is invalid, please choose from the above values");
            }
        } while (!validInput);
        return valueToReturn;
    }

    @Override
    public int readInt(String prompt) {
        String stringValue = null;
        int number = 0;
        boolean validInput = false;
        do {
            try {
                stringValue = readString(prompt);
                number = Integer.parseInt(stringValue);
                validInput = true;
            } catch (NumberFormatException e) {
                print(stringValue + " is not valid input, please try again");
            }
        } while (!validInput);
        return number;
    }

    @Override
    public int readInt(String prompt, int min) {
        int number = 0;
        do {
            number = readInt(prompt);
            // print error message becuase readInt does not
            if (number < min) {
                print(number + " is invalid, it can not be less than " + min);
            }
        } while (number < min);
        return number;
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int number = 0;
        do {
            number = readInt(prompt);
            if (number < min || number > max) {
                // message is printed because readInt() will not print if input is number but out off range
                print("Invalid input, the value must be between " + min + " and " + max);
            }
        } while (number < min || number > max);
        return number;
    }

    @Override
    public BigDecimal readBigDecimal(String prompt) {
        boolean validInput = false;
        BigDecimal result = null;
        do {
            String stringValue = null;
            try {
                stringValue = readString(prompt);
                result = new BigDecimal(stringValue);
                validInput = true;
            } catch (NumberFormatException ex) {
                print(stringValue + " is invalid input, please try again");
            }
        } while (!validInput);
        return result;
    }

    @Override
    public BigDecimal readBigDecimal(String prompt, BigDecimal minimumValue, boolean canBeBlank) {
        String userInput;
        BigDecimal result;
        boolean validInput = false;
        do {
            userInput = readString(prompt);
            // if true create bigdecimal with 0
            if (canBeBlank && userInput.isBlank()) {
                result = new BigDecimal("0");
                validInput = true;
            } else {
                result = new BigDecimal(userInput);
                // check input is not less than min value
                if (result.compareTo(minimumValue) < 0) {
                    print(result + " is invalid input, minimum must be " + minimumValue);
                    validInput = false;
                } else {
                    validInput = true;
                }
            }
        } while (!validInput);
        return result;
    }

    @Override
    public LocalDate readLocalDate(String prompt) {
        boolean validInput = false;
        LocalDate result = null;
        do {
            String stringValue = null;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                stringValue = readString(prompt);
                result = LocalDate.parse(stringValue, formatter);
                validInput = true;
            } catch (DateTimeParseException ex) {
                print(stringValue + " is not a valid date. it must be in (MM-DD-YYYY) format");
            }
        } while (!validInput);

        return result;
    }

    @Override
    public LocalDate readLocalDate(String prompt, LocalDate minDateAllowed) {
        LocalDate userDate;
        do {
            // read date
            userDate = readLocalDate(prompt);
            // check if date in future and display warning message if not
            if (minDateAllowed.compareTo(userDate) > 0) {
                // message displayed here because readLocalDate(prompt) shows message only when invalid date is entered
                print(userDate + " is invalid date, date must be in future");
            }
        } while (minDateAllowed.compareTo(userDate) > 0);
        return userDate;
    }

    @Override
    public boolean readNextOperation(String prompt) {
        String userInput;
        do {
            userInput = readString(prompt);
        } while (!userInput.equalsIgnoreCase("y")
                && !userInput.equalsIgnoreCase("yes")
                && !userInput.equalsIgnoreCase("n")
                && !userInput.equalsIgnoreCase("no"));
        // if yes return true
        if (userInput.equalsIgnoreCase("y") || userInput.equalsIgnoreCase("yes")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String readEnter(String prompt) {
        print(prompt);
        return console.nextLine();
    }
}
