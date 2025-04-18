package com.demo.bookstore.util;

import com.demo.bookstore.model.Author;
import com.demo.bookstore.model.Book;
import com.demo.bookstore.model.CartItem;
import com.demo.bookstore.model.Customer;
import com.demo.bookstore.model.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory data storage for all entities in the BookStore
 */
public class DataStore {
    private static final Map<Integer, Book> books = new HashMap<>();
    private static final Map<Integer, Author> authors = new HashMap<>();
    private static final Map<Integer, Customer> customers = new HashMap<>();
    private static final Map<Integer, Map<Integer, CartItem>> carts = new HashMap<>(); // customerId -> Map<bookId, CartItem>
    private static final Map<Integer, Order> orders = new HashMap<>();
    private static final Map<Integer, List<Order>> customerOrders = new HashMap<>(); // customerId -> List<Order>
    
    private static final AtomicInteger bookIdCounter = new AtomicInteger(1);
    private static final AtomicInteger authorIdCounter = new AtomicInteger(1);
    private static final AtomicInteger customerIdCounter = new AtomicInteger(1);
    private static final AtomicInteger orderIdCounter = new AtomicInteger(1);
    
    // Book methods
    public static List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }
    
    public static Book getBookById(int id) {
        return books.get(id);
    }
    
    public static Book addBook(Book book) {
        if (book.getId() <= 0) {
            book.setId(bookIdCounter.getAndIncrement());
        }
        books.put(book.getId(), book);
        return book;
    }
    
    public static Book updateBook(Book book) {
        books.put(book.getId(), book);
        return book;
    }
    
    public static void deleteBook(int id) {
        books.remove(id);
    }
    
    public static List<Book> getBooksByAuthor(int authorId) {
        List<Book> authorBooks = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthorId() == authorId) {
                authorBooks.add(book);
            }
        }
        return authorBooks;
    }
    
    // Author methods
    public static List<Author> getAllAuthors() {
        return new ArrayList<>(authors.values());
    }
    
    public static Author getAuthorById(int id) {
        return authors.get(id);
    }
    
    public static Author addAuthor(Author author) {
        if (author.getId() <= 0) {
            author.setId(authorIdCounter.getAndIncrement());
        }
        authors.put(author.getId(), author);
        return author;
    }
    
    public static Author updateAuthor(Author author) {
        authors.put(author.getId(), author);
        return author;
    }
    
    public static void deleteAuthor(int id) {
        authors.remove(id);
    }
    
    // Customer methods
    public static List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }
    
    public static Customer getCustomerById(int id) {
        return customers.get(id);
    }
    
    public static Customer addCustomer(Customer customer) {
        if (customer.getId() <= 0) {
            customer.setId(customerIdCounter.getAndIncrement());
        }
        customers.put(customer.getId(), customer);
        return customer;
    }
    
    public static Customer updateCustomer(Customer customer) {
        customers.put(customer.getId(), customer);
        return customer;
    }
    
    public static void deleteCustomer(int id) {
        customers.remove(id);
        carts.remove(id);
        customerOrders.remove(id);
    }
    
    // Cart methods
    public static void addItemToCart(int customerId, CartItem item) {
        Map<Integer, CartItem> cart = carts.computeIfAbsent(customerId, k -> new HashMap<>());
        cart.put(item.getBookId(), item);
    }
    
    public static List<CartItem> getCart(int customerId) {
        Map<Integer, CartItem> cart = carts.get(customerId);
        if (cart == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(cart.values());
    }
    
    public static void updateCartItem(int customerId, CartItem item) {
        Map<Integer, CartItem> cart = carts.get(customerId);
        if (cart != null) {
            cart.put(item.getBookId(), item);
        }
    }
    
    public static void removeCartItem(int customerId, int bookId) {
        Map<Integer, CartItem> cart = carts.get(customerId);
        if (cart != null) {
            cart.remove(bookId);
        }
    }
    
    public static void clearCart(int customerId) {
        Map<Integer, CartItem> cart = carts.get(customerId);
        if (cart != null) {
            cart.clear();
        }
    }
    
    // Order methods
    public static Order createOrder(int customerId, List<CartItem> items, double totalPrice) {
        Order order = new Order(orderIdCounter.getAndIncrement(), customerId, items, totalPrice);
        orders.put(order.getId(), order);
        
        List<Order> customerOrderList = customerOrders.computeIfAbsent(customerId, k -> new ArrayList<>());
        customerOrderList.add(order);
        
        return order;
    }
    
    public static List<Order> getCustomerOrders(int customerId) {
        return customerOrders.getOrDefault(customerId, new ArrayList<>());
    }
    
    public static Order getOrderById(int id) {
        return orders.get(id);
    }
    
    public static Order getCustomerOrderById(int customerId, int orderId) {
        Order order = orders.get(orderId);
        if (order != null && order.getCustomerId() == customerId) {
            return order;
        }
        return null;
    }
} 