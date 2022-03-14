package com.inetum.warehouse.Handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

@ControllerAdvice()
public class ExceptionHandlers {

    @ExceptionHandler
    public ResponseEntity<String> notFoundHandler(EntityNotFoundException e) {
        return new ResponseEntity(String.format("Not found product with code: %s", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<String> badCountHandler(ConstraintViolationException e) {
        return new ResponseEntity(String.format("count must be between 0 and 999"), HttpStatus.BAD_REQUEST);
    }

}
