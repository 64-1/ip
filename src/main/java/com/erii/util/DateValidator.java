package com.erii.util;

import java.time.LocalDate;

import com.erii.exception.DateTimeNotAfterCurrentTimeException;

public class DateValidator {
    
    /**
     * Validates that the given date is in the correct format.
     *
     * @param date The date to be validated.
     * @return true if the date is in the correct format, false otherwise.
     */
    public static void validateDateIsAfterCurrentTime(LocalDate userInputDateTime) {
        LocalDate currentTime = LocalDate.now();
        
        if (!userInputDateTime.isAfter(currentTime)) {
            throw new DateTimeNotAfterCurrentTimeException("\nThe provided date must be after the current date.");
        }
    }
}
