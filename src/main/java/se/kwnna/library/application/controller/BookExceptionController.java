package se.kwnna.library.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import se.kwnna.library.domain.exception.InvalidIsbnException;

@ControllerAdvice
public class BookExceptionController {

    @ExceptionHandler(value = InvalidIsbnException.class)
    public ResponseEntity<Object> exception(InvalidIsbnException exception) {
        return new ResponseEntity<>("Invalid ISBN format", HttpStatus.BAD_REQUEST);
    }
}
