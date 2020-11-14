package com.test.task.exceptions;

public class NonExistentIdException extends RuntimeException{
    public NonExistentIdException(String message) {
        super(message);
    }
}
