package ru.javawebinar.lunchvoting.util.exception;

import java.util.Arrays;

public class NotFoundException extends RuntimeException {
    private final String type;
    private final String msgCode;
    private final String[] args;

    public NotFoundException(String type, String msgCode, String... args) {
        super(String.format("type=%s, msgCode=%s, args=%s", type, msgCode, Arrays.toString(args)));
        this.type = type;
        this.msgCode = msgCode;
        this.args = args;
    }

    public NotFoundException(String... args) {
        this("Data not found", "Not found entity with {0}", args);
    }
}