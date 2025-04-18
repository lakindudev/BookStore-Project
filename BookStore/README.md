# BookStore API

A RESTful API for managing a bookstore, built with Java and JAX-RS (Jersey).

## Project Overview

This project implements a RESTful API for a Bookstore backend system with the following features:
- Book management (CRUD operations)
- Author management (CRUD operations)
- Customer management (CRUD operations)
- Shopping cart functionality
- Order processing

The API uses in-memory data storage and JSON for data input/output.

## Technologies

- Java 23
- JAX-RS (Jersey) for RESTful API implementation
- Grizzly HTTP server for running the application
- JSON for data serialization/deserialization

## Setup Instructions

### Prerequisites

- JDK 23 or higher
- NetBeans IDE
- Postman (for testing)

### Opening the Project in NetBeans

1. Open NetBeans
2. Click on "File" > "Open Project"
3. Navigate to the BookStore project folder and select it
4. Click "Open Project"

### Building and Running the Application

1. Once the project is open in NetBeans, right-click on the project in the Projects panel
2. Select "Clean and Build" to build the project
3. After building, right-click on the project again and select "Run"
4. The application will start and display a message in the console:
   ```
   BookStore API started at http://localhost:8080/api/
   Hit enter to stop the server...
   ```

## Testing with Postman

You can use Postman to test the API endpoints. Here are the available endpoints:

### Book Endpoints

- `POST /api/books` - Create a new book
  - Request body example:
    ```json
    {
      "title": "New Book Title",
      "authorId": 1,
      "isbn": "9781234567890",
      "publicationYear": 2023,
      "price": 19.99,
      "stock": 50
    }
    ```

- `GET /api/books` - Get all books
- `GET /api/books/{id}` - Get a book by ID
- `PUT /api/books/{id}` - Update a book
- `DELETE /api/books/{id}` - Delete a book

### Author Endpoints

- `POST /api/authors` - Create a new author
  - Request body example:
    ```json
    {
      "name": "Author Name",
      "biography": "Author biography text"
    }
    ```

- `GET /api/authors` - Get all authors
- `GET /api/authors/{id}` - Get an author by ID
- `PUT /api/authors/{id}` - Update an author
- `DELETE /api/authors/{id}` - Delete an author
- `GET /api/authors/{id}/books` - Get all books by an author

### Customer Endpoints

- `POST /api/customers` - Create a new customer
  - Request body example:
    ```json
    {
      "name": "Customer Name",
      "email": "customer@example.com",
      "password": "password123"
    }
    ```

- `GET /api/customers` - Get all customers
- `GET /api/customers/{id}` - Get a customer by ID
- `PUT /api/customers/{id}` - Update a customer
- `DELETE /api/customers/{id}` - Delete a customer

### Cart Endpoints

- `POST /api/customers/{customerId}/cart/items` - Add an item to cart
  - Request body example:
    ```json
    {
      "bookId": 1,
      "quantity": 2
    }
    ```

- `GET /api/customers/{customerId}/cart` - Get customer's cart
- `PUT /api/customers/{customerId}/cart/items/{bookId}` - Update cart item quantity
- `DELETE /api/customers/{customerId}/cart/items/{bookId}` - Remove item from cart

### Order Endpoints

- `POST /api/customers/{customerId}/orders` - Place an order (converts cart to order)
- `GET /api/customers/{customerId}/orders` - Get customer's orders
- `GET /api/customers/{customerId}/orders/{orderId}` - Get a specific order

## Sample Data

The application is pre-loaded with sample data:
- 3 authors (J.K. Rowling, George Orwell, Harper Lee)
- 5 books (Harry Potter books, 1984, Animal Farm, To Kill a Mockingbird)
- 2 customers (John Doe, Jane Smith)

You can start testing the API with these sample entities.

## Error Handling

The API provides proper error responses with appropriate HTTP status codes:
- 404 Not Found: when a requested resource doesn't exist
- 400 Bad Request: when input data is invalid or when a business rule is violated (e.g., insufficient stock)

Error responses are in JSON format with details about the error. 