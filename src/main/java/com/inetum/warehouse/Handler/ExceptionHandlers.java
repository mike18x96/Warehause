package com.inetum.warehouse.Handler;

import com.inetum.warehouse.exception.WrongRangeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice()
public class ExceptionHandlers {

    @ExceptionHandler
    public ResponseEntity<String> notFoundHandler(EntityNotFoundException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WrongRangeException.class)
    public ResponseEntity<String> handleException(WrongRangeException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


}
