package com.test.task.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler
    public ResponseEntity<?> handleIdException(nonExistentIdException exc) {
        log.error(exc.getMessage(), exc);
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleSomeException(RuntimeException exc) {
        log.error(exc.getMessage(), exc);
        return new ResponseEntity<>("Sorry, an error occurred on the server", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleBadEntityException(MalformedEntityException exc) {
        log.error(exc.getMessage(), exc);
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleNullIdException(NullIdException exc) {
        log.error(exc.getMessage(), exc);
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleMalformedJSONFromFrontEnd(HttpMessageNotReadableException exc) {
        log.error(exc.getMessage(), exc);
        return new ResponseEntity<>("Ошибка в формировании JSON объекта", HttpStatus.I_AM_A_TEAPOT);
    }
}
