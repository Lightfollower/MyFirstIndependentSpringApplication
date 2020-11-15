package com.test.task.exceptions;

public class NullIdException extends RuntimeException {
    public NullIdException() {
        super("Id can't be null");
    }
}
