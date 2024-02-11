package com.abdelalielbihari.portfolio.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionController extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleAllExceptions(Exception ex) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        return new ResponseEntity<>("Null pointer exception: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ResponseEntity<String> handleArrayIndexOutOfBoundsException(ArrayIndexOutOfBoundsException ex) {
        return new ResponseEntity<>("Array index out of bounds exception: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleNumberFormatException(NumberFormatException ex) {
        return new ResponseEntity<>("Number format exception: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex) {
        return new ResponseEntity<>("IO exception: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleSQLException(SQLException ex) {
        return new ResponseEntity<>("SQL exception: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>("Illegal argument exception: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return new ResponseEntity<>("Illegal state exception: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return new ResponseEntity<>("No such element exception: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<String> handleUnsupportedOperationException(UnsupportedOperationException ex) {
        return new ResponseEntity<>("Unsupported operation exception: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<String> handleClassCastException(ClassCastException ex) {
        return new ResponseEntity<>("Class cast exception: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}