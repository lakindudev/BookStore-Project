package com.demo.bookstore.exception;

/**
 * Exception thrown when a book is out of stock
 */
public class OutOfStockException extends RuntimeException {
    
    public OutOfStockException(String message) {
        super(message);
    }
    
    public OutOfStockException(int bookId, int requestedQuantity, int availableStock) {
        super("Book with ID " + bookId + " has insufficient stock. Requested: " + requestedQuantity + ", Available: " + availableStock);
    }
} 