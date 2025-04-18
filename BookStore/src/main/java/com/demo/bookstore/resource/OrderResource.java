package com.demo.bookstore.resource;

import com.demo.bookstore.exception.BookNotFoundException;
import com.demo.bookstore.exception.CartNotFoundException;
import com.demo.bookstore.exception.CustomerNotFoundException;
import com.demo.bookstore.exception.InvalidInputException;
import com.demo.bookstore.exception.OutOfStockException;
import com.demo.bookstore.model.Book;
import com.demo.bookstore.model.CartItem;
import com.demo.bookstore.model.Customer;
import com.demo.bookstore.model.Order;
import com.demo.bookstore.util.DataStore;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Resource class for Order operations
 */
@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    /**
     * Place a new order from the customer's cart
     * @param customerId ID of the customer
     * @return Response with created order
     */
    @POST
    public Response placeOrder(@PathParam("customerId") int customerId) {
        Customer customer = DataStore.getCustomerById(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException(customerId);
        }
        
        List<CartItem> cart = DataStore.getCart(customerId);
        if (cart.isEmpty()) {
            throw new CartNotFoundException(customerId);
        }
        
        // Validate stock and calculate total price
        double totalPrice = 0.0;
        List<CartItem> orderItems = new ArrayList<>();
        
        for (CartItem item : cart) {
            Book book = DataStore.getBookById(item.getBookId());
            if (book == null) {
                throw new BookNotFoundException(item.getBookId());
            }
            
            if (book.getStock() < item.getQuantity()) {
                throw new OutOfStockException(book.getId(), item.getQuantity(), book.getStock());
            }
            
            totalPrice += book.getPrice() * item.getQuantity();
            
            // Update book stock
            book.setStock(book.getStock() - item.getQuantity());
            DataStore.updateBook(book);
            
            // Add to order items
            orderItems.add(new CartItem(item.getBookId(), item.getQuantity()));
        }
        
        // Create the order
        Order order = DataStore.createOrder(customerId, orderItems, totalPrice);
        
        // Clear the cart
        DataStore.clearCart(customerId);
        
        return Response.status(Status.CREATED).entity(order).build();
    }
    
    /**
     * Get all orders for a customer
     * @param customerId ID of the customer
     * @return List of orders
     */
    @GET
    public List<Order> getCustomerOrders(@PathParam("customerId") int customerId) {
        Customer customer = DataStore.getCustomerById(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException(customerId);
        }
        
        return DataStore.getCustomerOrders(customerId);
    }
    
    /**
     * Get a specific order for a customer
     * @param customerId ID of the customer
     * @param orderId ID of the order
     * @return Order details
     */
    @GET
    @Path("/{orderId}")
    public Order getOrder(
            @PathParam("customerId") int customerId,
            @PathParam("orderId") int orderId) {
        
        Customer customer = DataStore.getCustomerById(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException(customerId);
        }
        
        Order order = DataStore.getCustomerOrderById(customerId, orderId);
        if (order == null) {
            throw new InvalidInputException("Order not found or doesn't belong to the customer");
        }
        
        return order;
    }
} 