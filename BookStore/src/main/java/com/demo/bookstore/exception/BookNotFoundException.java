package com.demo.bookstore.exception;

/**
 * Exception thrown when a book is not found
 */
public class BookNotFoundException extends RuntimeException {
    
    public BookNotFoundException(String message) {
        super(message);
    }
    
    public BookNotFoundException(int id) {
        super("Book with ID " + id + " not found");
    }
} 