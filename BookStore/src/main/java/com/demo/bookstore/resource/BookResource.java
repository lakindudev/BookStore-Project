package com.demo.bookstore.resource;

import com.demo.bookstore.exception.AuthorNotFoundException;
import com.demo.bookstore.exception.BookNotFoundException;
import com.demo.bookstore.exception.InvalidInputException;
import com.demo.bookstore.model.Author;
import com.demo.bookstore.model.Book;
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
 * Resource class for Book entity
 */
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    private static final Logger LOGGER = Logger.getLogger(BookResource.class.getName());

    /**
     * Create a new book
     * @param book Book object to create
     * @return Response with created book
     */
    @POST
    public Response createBook(Book book) {
        LOGGER.info("Received request to create book: " + (book != null ? book.getTitle() : "null"));
        
        if (book == null || book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            LOGGER.warning("Invalid book creation request: missing title");
            throw new InvalidInputException("Book title is required");
        }
        
        if (book.getAuthorId() <= 0) {
            LOGGER.warning("Invalid book creation request: invalid author ID: " + book.getAuthorId());
            throw new InvalidInputException("Valid author ID is required");
        }
        
        // Check if author exists before adding the book
        Author author = DataStore.getAuthorById(book.getAuthorId());
        if (author == null) {
            LOGGER.warning("Book creation failed: author with ID " + book.getAuthorId() + " not found");
            throw new AuthorNotFoundException("Cannot add book. Author with ID " + book.getAuthorId() + " does not exist");
        }
        
        // Validate publication year is not in the future
        int currentYear = java.time.Year.now().getValue();
        if (book.getPublicationYear() > currentYear) {
            LOGGER.warning("Invalid book creation request: future publication year: " + book.getPublicationYear());
            throw new InvalidInputException("Publication year cannot be in the future");
        }
        
        if (book.getPrice() < 0) {
            LOGGER.warning("Invalid book creation request: negative price: " + book.getPrice());
            throw new InvalidInputException("Book price cannot be negative");
        }
        
        if (book.getStock() < 0) {
            LOGGER.warning("Invalid book creation request: negative stock: " + book.getStock());
            throw new InvalidInputException("Book stock cannot be negative");
        }
        
        Book createdBook = DataStore.addBook(book);
        LOGGER.info("Book created successfully: ID=" + createdBook.getId() + ", Title=" + createdBook.getTitle());
        return Response.status(Status.CREATED).entity(createdBook).build();
    }
    
    /**
     * Get all books
     * @return List of all books
     */
    @GET
    public List<Book> getAllBooks() {
        LOGGER.info("Retrieving all books");
        return DataStore.getAllBooks();
    }
    
    /**
     * Get a book by ID
     * @param id ID of the book to retrieve
     * @return Book with the specified ID
     */
    @GET
    @Path("/{id}")
    public Book getBookById(@PathParam("id") int id) {
        LOGGER.info("Retrieving book with ID: " + id);
        
        Book book = DataStore.getBookById(id);
        if (book == null) {
            LOGGER.warning("Book with ID " + id + " not found");
            throw new BookNotFoundException(id);
        }
        return book;
    }
    
    /**
     * Update an existing book
     * @param id ID of the book to update
     * @param book Updated book data
     * @return Response with updated book
     */
    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") int id, Book book) {
        LOGGER.info("Received request to update book with ID: " + id);
        
        if (book == null) {
            LOGGER.warning("Invalid book update request: book data is null");
            throw new InvalidInputException("Book data is required");
        }
        
        Book existingBook = DataStore.getBookById(id);
        if (existingBook == null) {
            LOGGER.warning("Book update failed: book with ID " + id + " not found");
            throw new BookNotFoundException(id);
        }
        
        // Validate publication year is not in the future
        int currentYear = java.time.Year.now().getValue();
        if (book.getPublicationYear() > currentYear) {
            LOGGER.warning("Invalid book update request: future publication year: " + book.getPublicationYear());
            throw new InvalidInputException("Publication year cannot be in the future");
        }
        
        book.setId(id);
        Book updatedBook = DataStore.updateBook(book);
        
        LOGGER.info("Book updated successfully: ID=" + updatedBook.getId() + ", Title=" + updatedBook.getTitle());
        return Response.ok(updatedBook).build();
    }
    
    /**
     * Delete a book
     * @param id ID of the book to delete
     * @return Response indicating success
     */
    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        LOGGER.info("Received request to delete book with ID: " + id);
        
        Book book = DataStore.getBookById(id);
        if (book == null) {
            LOGGER.warning("Book deletion failed: book with ID " + id + " not found");
            throw new BookNotFoundException(id);
        }
        
        DataStore.deleteBook(id);
        
        LOGGER.info("Book deleted successfully: ID=" + id);
        return Response.noContent().build();
    }
} 