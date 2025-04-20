package com.demo.bookstore.util;

import com.demo.bookstore.model.Author;
import com.demo.bookstore.model.Book;
import com.demo.bookstore.model.Customer;

/**
 * Utility class that initializes sample data for the BookStore API
 */
public class DataInitializer {
    
    private static boolean initialized = false;
    
    /**
     * Initialize sample data for books, authors, and customers
     */
    public static void initData() {
        if (initialized) {
            return;
        }
        
        // Create sample authors
        Author author1 = new Author(0, "J.K.", "Rowling", "British author best known for the Harry Potter series");
        Author author2 = new Author(0, "George", "Orwell", "English novelist and essayist, journalist and critic");
        Author author3 = new Author(0, "Harper", "Lee", "American novelist widely known for her novel To Kill a Mockingbird");
        
        author1 = DataStore.addAuthor(author1);
        author2 = DataStore.addAuthor(author2);
        author3 = DataStore.addAuthor(author3);
        
        // Create sample books
        Book book1 = new Book(0, "Harry Potter and the Philosopher's Stone", author1.getId(), "9780747532743", 1997, 19.99, 50);
        Book book2 = new Book(0, "Harry Potter and the Chamber of Secrets", author1.getId(), "9780747538486", 1998, 19.99, 40);
        Book book3 = new Book(0, "1984", author2.getId(), "9780451524935", 1949, 14.99, 30);
        Book book4 = new Book(0, "Animal Farm", author2.getId(), "9780451526342", 1945, 12.99, 25);
        Book book5 = new Book(0, "To Kill a Mockingbird", author3.getId(), "9780061120084", 1960, 15.99, 35);
        
        DataStore.addBook(book1);
        DataStore.addBook(book2);
        DataStore.addBook(book3);
        DataStore.addBook(book4);
        DataStore.addBook(book5);
        
        // Create sample customers
        Customer customer1 = new Customer(0, "John", "Doe", "john.doe@example.com", "password123");
        Customer customer2 = new Customer(0, "Jane", "Smith", "jane.smith@example.com", "password456");
        
        DataStore.addCustomer(customer1);
        DataStore.addCustomer(customer2);
        
        initialized = true;
    }
} 