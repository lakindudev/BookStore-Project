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

/**
 * Resource class for Author entity
 */
@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    /**
     * Create a new author
     * @param author Author object to create
     * @return Response with created author
     */
    @POST
    public Response createAuthor(Author author) {
        if (author == null || author.getName() == null || author.getName().trim().isEmpty()) {
            throw new InvalidInputException("Author name is required");
        }
        
        Author createdAuthor = DataStore.addAuthor(author);
        return Response.status(Status.CREATED).entity(createdAuthor).build();
    }
    
    /**
     * Get all authors
     * @return List of all authors
     */
    @GET
    public List<Author> getAllAuthors() {
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
        Author author = DataStore.getAuthorById(id);
        if (author == null) {
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
        if (author == null) {
            throw new InvalidInputException("Author data is required");
        }
        
        Author existingAuthor = DataStore.getAuthorById(id);
        if (existingAuthor == null) {
            throw new AuthorNotFoundException(id);
        }
        
        author.setId(id);
        Author updatedAuthor = DataStore.updateAuthor(author);
        
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
        Author author = DataStore.getAuthorById(id);
        if (author == null) {
            throw new AuthorNotFoundException(id);
        }
        
        DataStore.deleteAuthor(id);
        
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
        Author author = DataStore.getAuthorById(id);
        if (author == null) {
            throw new AuthorNotFoundException(id);
        }
        
        return DataStore.getBooksByAuthor(id);
    }
} 