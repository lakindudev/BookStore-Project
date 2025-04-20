package com.demo.bookstore.resource;

import com.demo.bookstore.exception.AuthorNotFoundException;
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
 * Resource class for Author entity
 */
@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    private static final Logger LOGGER = Logger.getLogger(AuthorResource.class.getName());

    /**
     * Create a new author
     * @param author Author object to create
     * @return Response with created author
     */
    @POST
    public Response createAuthor(Author author) {
        LOGGER.info("Received request to create author");
        
        if (author == null || author.getFirstName() == null || author.getFirstName().trim().isEmpty() ||
            author.getLastName() == null || author.getLastName().trim().isEmpty()) {
            LOGGER.warning("Invalid author creation request: missing first name or last name");
            throw new InvalidInputException("Author first name and last name are required");
        }
        
        Author createdAuthor = DataStore.addAuthor(author);
        LOGGER.info("Author created successfully: ID=" + createdAuthor.getId() + 
                   ", Name=" + createdAuthor.getFirstName() + " " + createdAuthor.getLastName());
        return Response.status(Status.CREATED).entity(createdAuthor).build();
    }
    
    /**
     * Get all authors
     * @return List of all authors
     */
    @GET
    public List<Author> getAllAuthors() {
        LOGGER.info("Retrieving all authors");
        return DataStore.getAllAuthors();
    }
    
    /**
     * Get an author by ID
     * @param id ID of the author to retrieve
     * @return Author with the specified ID
     */
    @GET
    @Path("/{id}")
    public Author getAuthorById(@PathParam("id") int id) {
        LOGGER.info("Retrieving author with ID: " + id);
        
        Author author = DataStore.getAuthorById(id);
        if (author == null) {
            LOGGER.warning("Author with ID " + id + " not found");
            throw new AuthorNotFoundException(id);
        }
        return author;
    }
    
    /**
     * Update an existing author
     * @param id ID of the author to update
     * @param author Updated author data
     * @return Response with updated author
     */
    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") int id, Author author) {
        LOGGER.info("Received request to update author with ID: " + id);
        
        if (author == null) {
            LOGGER.warning("Invalid author update request: author data is null");
            throw new InvalidInputException("Author data is required");
        }
        
        Author existingAuthor = DataStore.getAuthorById(id);
        if (existingAuthor == null) {
            LOGGER.warning("Author update failed: author with ID " + id + " not found");
            throw new AuthorNotFoundException(id);
        }
        
        author.setId(id);
        Author updatedAuthor = DataStore.updateAuthor(author);
        
        LOGGER.info("Author updated successfully: ID=" + updatedAuthor.getId() + 
                   ", Name=" + updatedAuthor.getFirstName() + " " + updatedAuthor.getLastName());
        return Response.ok(updatedAuthor).build();
    }
    
    /**
     * Delete an author
     * @param id ID of the author to delete
     * @return Response indicating success
     */
    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        LOGGER.info("Received request to delete author with ID: " + id);
        
        Author author = DataStore.getAuthorById(id);
        if (author == null) {
            LOGGER.warning("Author deletion failed: author with ID " + id + " not found");
            throw new AuthorNotFoundException(id);
        }
        
        DataStore.deleteAuthor(id);
        
        LOGGER.info("Author deleted successfully: ID=" + id);
        return Response.noContent().build();
    }
    
    /**
     * Get all books by an author
     * @param id ID of the author
     * @return List of books by the author
     */
    @GET
    @Path("/{id}/books")
    public List<Book> getAuthorBooks(@PathParam("id") int id) {
        LOGGER.info("Retrieving books for author with ID: " + id);
        
        Author author = DataStore.getAuthorById(id);
        if (author == null) {
            LOGGER.warning("Author with ID " + id + " not found when retrieving their books");
            throw new AuthorNotFoundException(id);
        }
        
        List<Book> books = DataStore.getBooksByAuthor(id);
        LOGGER.info("Retrieved " + books.size() + " books for author ID=" + id);
        return books;
    }
} 