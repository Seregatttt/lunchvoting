package ru.javawebinar.lunchvoting;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimeFactory {
    public static final LocalTime VOTE_TIME_LIMIT = LocalTime.of(11, 00);

    public LocalTime getCurrentTime() {
        return LocalTime.now();
    }

    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    public LocalTime getTimeLimit() {
        return VOTE_TIME_LIMIT;
    }
}