package com.demo.bookstore.exception;

/**
 * Exception thrown when a customer's cart is not found
 */
public class CartNotFoundException extends RuntimeException {
    
    public CartNotFoundException(String message) {
        super(message);
    }
    
    public CartNotFoundException(int customerId) {
        super("Cart for customer with ID " + customerId + " not found");
    }
} 