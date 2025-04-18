package com.demo.bookstore.exception;

/**
 * Exception thrown when an author is not found
 */
public class AuthorNotFoundException extends RuntimeException {
    
    public AuthorNotFoundException(String message) {
        super(message);
    }
    
    public AuthorNotFoundException(int id) {
        super("Author with ID " + id + " not found");
    }
} 