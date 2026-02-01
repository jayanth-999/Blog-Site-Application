package com.blogsite.userservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ========================================
 * USER REGISTRATION REQUEST DTO
 * ========================================
 * 
 * DTO = Data Transfer Object
 * 
 * WHAT IS A DTO?
 * - A simple object that carries data between processes
 * - Used for transferring data over the network
 * - Contains no business logic, just data and validation
 * 
 * WHY USE DTOs INSTEAD OF ENTITIES?
 * 1. Security: Don't expose internal database structure
 * 2. Flexibility: Can have different fields than the entity
 * 3. Validation: Can have different validation rules for requests
 * 4. Clean API: Client only sees what they need
 * 
 * EXAMPLE:
 * Entity might have: id, createdAt, updatedAt (not needed in request)
 * DTO only has: userName, userEmail, password (what client sends)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {

    /**
     * User name field
     * Same validation as entity, but this is what comes from the client
     */
    @NotBlank(message = "User name is required")
    @Size(min = 3, max = 50, message = "User name must be between 3 and 50 characters")
    private String userName;

    /**
     * Email field
     * Must contain @ and end with .com as per requirements
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Pattern(regexp = ".*@.*\\.com$", message = "Email must contain @ and end with .com")
    private String userEmail;

    /**
     * Password field
     * Must be alphanumeric and at least 8 characters
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$", 
             message = "Password must be alphanumeric (letters and numbers)")
    private String password;
}
