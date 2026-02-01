package com.blogsite.userservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * ========================================
 * CUSTOM EXCEPTION - USER ALREADY EXISTS
 * ========================================
 * 
 * WHAT IS A CUSTOM EXCEPTION?
 * - A specialized exception for specific error scenarios
 * - Makes your code more readable and maintainable
 * - Allows different handling for different error types
 * 
 * WHY CREATE CUSTOM EXCEPTIONS?
 * Instead of: throw new RuntimeException("User exists");
 * You can: throw new UserAlreadyExistsException("Email already registered");
 * 
 * BENEFITS:
 * - Clear intent: other developers know exactly what went wrong
 * - Type-safe: can catch specific exceptions
 * - Can include additional data (like HTTP status code)
 * 
 * @Getter from Lombok generates getStatus() method
 */
@Getter
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * HTTP status code to return to client
     * 409 CONFLICT = Resource already exists
     */
    private final HttpStatus status = HttpStatus.CONFLICT;

    /**
     * Constructor with custom message
     * 
     * @param message Descriptive error message
     */
    public UserAlreadyExistsException(String message) {
        super(message);  // Pass message to parent RuntimeException class
    }
}
