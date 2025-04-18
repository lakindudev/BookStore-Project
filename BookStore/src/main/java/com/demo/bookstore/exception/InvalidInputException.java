package com.demo.bookstore.exception;

/**
 * Exception thrown when input data is invalid
 */
public class InvalidInputException extends RuntimeException {
    
    public InvalidInputException(String message) {
        super(message);
    }
} 