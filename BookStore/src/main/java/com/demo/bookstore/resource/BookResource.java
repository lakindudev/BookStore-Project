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

/**
 * Resource class for Book entity
 */
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    /**
     * Create a new book
     * @param book Book object to create
     * @return Response with created book
     */
    @POST
    public Response createBook(Book book) {
        if (book == null || book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Book title is required");
        }
        
        if (book.getAuthorId() <= 0) {
            throw new InvalidInputException("Valid author ID is required");
        }
        
        // Check if author exists before adding the book
        Author author = DataStore.getAuthorById(book.getAuthorId());
        if (author == null) {
            throw new AuthorNotFoundException("Cannot add book. Author with ID " + book.getAuthorId() + " does not exist");
        }
        
        if (book.getPrice() < 0) {
            throw new InvalidInputException("Book price cannot be negative");
        }
        
        if (book.getStock() < 0) {
            throw new InvalidInputException("Book stock cannot be negative");
        }
        
        Book createdBook = DataStore.addBook(book);
        return Response.status(Status.CREATED).entity(createdBook).build();
    }
    
    /**
     * Get all books
     * @return List of all books
     */
    @GET
    public List<Book> getAllBooks() {
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
        Book book = DataStore.getBookById(id);
        if (book == null) {
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
        if (book == null) {
            throw new InvalidInputException("Book data is required");
        }
        
        Book existingBook = DataStore.getBookById(id);
        if (existingBook == null) {
            throw new BookNotFoundException(id);
        }
        
        book.setId(id);
        Book updatedBook = DataStore.updateBook(book);
        
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
        Book book = DataStore.getBookById(id);
        if (book == null) {
            throw new BookNotFoundException(id);
        }
        
        DataStore.deleteBook(id);
        
        return Response.noContent().build();
    }
} 