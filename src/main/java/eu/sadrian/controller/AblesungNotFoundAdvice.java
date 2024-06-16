package eu.sadrian.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AblesungNotFoundAdvice {

    @ExceptionHandler(AblesungNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String ablesungNotFoundHandler(AblesungNotFoundException ex) {
        return ex.getMessage();
    }
}
