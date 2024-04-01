package com.erii.util;

import java.time.LocalDateTime;
import com.erii.exception.DateTimeNotAfterCurrentTimeException;

public class DateTimeValidator {

    /**
     * Validates that the given date and time are after the current system date and time.
     *
     * @param userInputDateTime The date and time to be validated.
     * @throws DateTimeNotAfterCurrentTimeException If the userInputDateTime is not after the current time.
     */
    public static void validateDateTimeIsAfterCurrentTime(LocalDateTime userInputDateTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        
        if (!userInputDateTime.isAfter(currentTime)) {
            throw new DateTimeNotAfterCurrentTimeException("\nThe provided date and time must be after the current time.");
        }
    }

}
