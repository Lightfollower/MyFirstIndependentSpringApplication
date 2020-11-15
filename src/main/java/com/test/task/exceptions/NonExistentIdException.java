package com.test.task.exceptions;

public class nonExistentIdException extends RuntimeException{
    public nonExistentIdException(String message) {
        super(message);
    }
}
