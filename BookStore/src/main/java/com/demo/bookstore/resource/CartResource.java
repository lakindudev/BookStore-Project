package com.demo.bookstore.resource;

import com.demo.bookstore.exception.BookNotFoundException;
import com.demo.bookstore.exception.CartNotFoundException;
import com.demo.bookstore.exception.CustomerNotFoundException;
import com.demo.bookstore.exception.InvalidInputException;
import com.demo.bookstore.exception.OutOfStockException;
import com.demo.bookstore.model.Book;
import com.demo.bookstore.model.CartItem;
import com.demo.bookstore.model.Customer;
import com.demo.bookstore.util.DataStore;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.util.List;
import java.util.logging.Logger;

/**
 * Resource class for shopping cart operations
 */
@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {

    private static final Logger LOGGER = Logger.getLogger(CartResource.class.getName());

    /**
     * Add a book to the customer's cart
     * @param customerId ID of the customer
     * @param cartItem Cart item to add
     * @return Response with success message
     */
    @POST
    @Path("/items")
    public Response addToCart(@PathParam("customerId") int customerId, CartItem cartItem) {
        LOGGER.info("Received request to add item to cart for customer ID: " + customerId);
        
        if (cartItem == null || cartItem.getBookId() <= 0 || cartItem.getQuantity() <= 0) {
            LOGGER.warning("Invalid cart item: bookId=" + (cartItem != null ? cartItem.getBookId() : "null") + 
                         ", quantity=" + (cartItem != null ? cartItem.getQuantity() : "null"));
            throw new InvalidInputException("Valid book ID and quantity are required");
        }
        
        Customer customer = DataStore.getCustomerById(customerId);
        if (customer == null) {
            LOGGER.warning("Add to cart failed: customer with ID " + customerId + " not found");
            throw new CustomerNotFoundException(customerId);
        }
        
        Book book = DataStore.getBookById(cartItem.getBookId());
        if (book == null) {
            LOGGER.warning("Add to cart failed: book with ID " + cartItem.getBookId() + " not found");
            throw new BookNotFoundException(cartItem.getBookId());
        }
        
        if (book.getStock() < cartItem.getQuantity()) {
            LOGGER.warning("Add to cart failed: insufficient stock for book ID " + book.getId() + 
                         " (requested: " + cartItem.getQuantity() + ", available: " + book.getStock() + ")");
            throw new OutOfStockException(book.getId(), cartItem.getQuantity(), book.getStock());
        }
        
        DataStore.addItemToCart(customerId, cartItem);
        
        LOGGER.info("Item added to cart successfully: customer ID=" + customerId + 
                   ", book ID=" + cartItem.getBookId() + ", quantity=" + cartItem.getQuantity());
        return Response.status(Status.CREATED).entity(cartItem).build();
    }
    
    /**
     * Get the customer's cart
     * @param customerId ID of the customer
     * @return List of cart items
     */
    @GET
    public List<CartItem> getCart(@PathParam("customerId") int customerId) {
        LOGGER.info("Retrieving cart for customer ID: " + customerId);
        
        Customer customer = DataStore.getCustomerById(customerId);
        if (customer == null) {
            LOGGER.warning("Get cart failed: customer with ID " + customerId + " not found");
            throw new CustomerNotFoundException(customerId);
        }
        
        List<CartItem> cart = DataStore.getCart(customerId);
        if (cart.isEmpty()) {
            LOGGER.warning("Cart not found for customer ID: " + customerId);
            throw new CartNotFoundException(customerId);
        }
        
        LOGGER.info("Retrieved " + cart.size() + " cart items for customer ID=" + customerId);
        return cart;
    }
    
    /**
     * Update a cart item quantity
     * @param customerId ID of the customer
     * @param bookId ID of the book to update
     * @param cartItem Updated cart item
     * @return Response with updated cart item
     */
    @PUT
    @Path("/items/{bookId}")
    public Response updateCartItem(
            @PathParam("customerId") int customerId,
            @PathParam("bookId") int bookId,
            CartItem cartItem) {
        
        LOGGER.info("Received request to update cart item: customer ID=" + customerId + 
                   ", book ID=" + bookId);
        
        if (cartItem == null || cartItem.getQuantity() <= 0) {
            LOGGER.warning("Invalid cart item update: quantity=" + 
                         (cartItem != null ? cartItem.getQuantity() : "null"));
            throw new InvalidInputException("Valid quantity is required");
        }
        
        Customer customer = DataStore.getCustomerById(customerId);
        if (customer == null) {
            LOGGER.warning("Update cart item failed: customer with ID " + customerId + " not found");
            throw new CustomerNotFoundException(customerId);
        }
        
        Book book = DataStore.getBookById(bookId);
        if (book == null) {
            LOGGER.warning("Update cart item failed: book with ID " + bookId + " not found");
            throw new BookNotFoundException(bookId);
        }
        
        List<CartItem> cart = DataStore.getCart(customerId);
        boolean itemExists = false;
        for (CartItem item : cart) {
            if (item.getBookId() == bookId) {
                itemExists = true;
                break;
            }
        }
        
        if (!itemExists) {
            LOGGER.warning("Update cart item failed: item does not exist in the cart");
            throw new InvalidInputException("Item does not exist in the cart");
        }
        
        if (book.getStock() < cartItem.getQuantity()) {
            LOGGER.warning("Update cart item failed: insufficient stock for book ID " + bookId + 
                         " (requested: " + cartItem.getQuantity() + ", available: " + book.getStock() + ")");
            throw new OutOfStockException(bookId, cartItem.getQuantity(), book.getStock());
        }
        
        cartItem.setBookId(bookId);
        DataStore.updateCartItem(customerId, cartItem);
        
        LOGGER.info("Cart item updated successfully: customer ID=" + customerId + 
                   ", book ID=" + bookId + ", new quantity=" + cartItem.getQuantity());
        return Response.ok(cartItem).build();
    }
    
    /**
     * Remove an item from the cart
     * @param customerId ID of the customer
     * @param bookId ID of the book to remove
     * @return Response indicating success
     */
    @DELETE
    @Path("/items/{bookId}")
    public Response removeCartItem(
            @PathParam("customerId") int customerId,
            @PathParam("bookId") int bookId) {
        
        LOGGER.info("Received request to remove item from cart: customer ID=" + customerId + 
                   ", book ID=" + bookId);
        
        Customer customer = DataStore.getCustomerById(customerId);
        if (customer == null) {
            LOGGER.warning("Remove cart item failed: customer with ID " + customerId + " not found");
            throw new CustomerNotFoundException(customerId);
        }
        
        Book book = DataStore.getBookById(bookId);
        if (book == null) {
            LOGGER.warning("Remove cart item failed: book with ID " + bookId + " not found");
            throw new BookNotFoundException(bookId);
        }
        
        DataStore.removeCartItem(customerId, bookId);
        
        LOGGER.info("Cart item removed successfully: customer ID=" + customerId + ", book ID=" + bookId);
        return Response.noContent().build();
    }
} 