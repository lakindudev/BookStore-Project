package com.demo.bookstore.resource;

import com.demo.bookstore.exception.CustomerNotFoundException;
import com.demo.bookstore.exception.InvalidInputException;
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
 * Resource class for Customer entity
 */
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private static final Logger LOGGER = Logger.getLogger(CustomerResource.class.getName());

    /**
     * Create a new customer
     * @param customer Customer object to create
     * @return Response with created customer
     */
    @POST
    public Response createCustomer(Customer customer) {
        LOGGER.info("Received request to create customer");
        
        if (customer == null || customer.getFirstName() == null || customer.getFirstName().trim().isEmpty() ||
            customer.getLastName() == null || customer.getLastName().trim().isEmpty()) {
            LOGGER.warning("Invalid customer creation request: missing first name or last name");
            throw new InvalidInputException("Customer first name and last name are required");
        }
        
        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            LOGGER.warning("Invalid customer creation request: missing email");
            throw new InvalidInputException("Customer email is required");
        }
        
        if (customer.getPassword() == null || customer.getPassword().trim().isEmpty()) {
            LOGGER.warning("Invalid customer creation request: missing password");
            throw new InvalidInputException("Customer password is required");
        }
        
        Customer createdCustomer = DataStore.addCustomer(customer);
        LOGGER.info("Customer created successfully: ID=" + createdCustomer.getId() + 
                   ", Email=" + createdCustomer.getEmail());
        return Response.status(Status.CREATED).entity(createdCustomer).build();
    }
    
    /**
     * Get all customers
     * @return List of all customers
     */
    @GET
    public List<Customer> getAllCustomers() {
        LOGGER.info("Retrieving all customers");
        return DataStore.getAllCustomers();
    }
    
    /**
     * Get a customer by ID
     * @param id ID of the customer to retrieve
     * @return Customer with the specified ID
     */
    @GET
    @Path("/{id}")
    public Customer getCustomerById(@PathParam("id") int id) {
        LOGGER.info("Retrieving customer with ID: " + id);
        
        Customer customer = DataStore.getCustomerById(id);
        if (customer == null) {
            LOGGER.warning("Customer with ID " + id + " not found");
            throw new CustomerNotFoundException(id);
        }
        return customer;
    }
    
    /**
     * Update an existing customer
     * @param id ID of the customer to update
     * @param customer Updated customer data
     * @return Response with updated customer
     */
    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") int id, Customer customer) {
        LOGGER.info("Received request to update customer with ID: " + id);
        
        if (customer == null) {
            LOGGER.warning("Invalid customer update request: customer data is null");
            throw new InvalidInputException("Customer data is required");
        }
        
        Customer existingCustomer = DataStore.getCustomerById(id);
        if (existingCustomer == null) {
            LOGGER.warning("Customer update failed: customer with ID " + id + " not found");
            throw new CustomerNotFoundException(id);
        }
        
        customer.setId(id);
        Customer updatedCustomer = DataStore.updateCustomer(customer);
        
        LOGGER.info("Customer updated successfully: ID=" + updatedCustomer.getId() + 
                   ", Email=" + updatedCustomer.getEmail());
        return Response.ok(updatedCustomer).build();
    }
    
    /**
     * Delete a customer
     * @param id ID of the customer to delete
     * @return Response indicating success
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        LOGGER.info("Received request to delete customer with ID: " + id);
        
        Customer customer = DataStore.getCustomerById(id);
        if (customer == null) {
            LOGGER.warning("Customer deletion failed: customer with ID " + id + " not found");
            throw new CustomerNotFoundException(id);
        }
        
        DataStore.deleteCustomer(id);
        
        LOGGER.info("Customer deleted successfully: ID=" + id);
        return Response.noContent().build();
    }
} 