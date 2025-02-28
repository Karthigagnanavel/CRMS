package com.app.crms.exception;

public class NumberFormatUtil{
	public static int parseInteger(String input) throws InvalidNumberFormatException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new InvalidNumberFormatException("Invalid number format: " + input);
        }
    }
}
