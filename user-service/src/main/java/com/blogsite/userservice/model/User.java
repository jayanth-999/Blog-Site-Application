package com.blogsite.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ========================================
 * USER ENTITY - DATABASE MODEL
 * ========================================
 * 
 * This class represents a User in our system.
 * It's both:
 * 1. A Java class (POJO - Plain Old Java Object)
 * 2. A database table (thanks to JPA annotations)
 * 
 * HOW IT WORKS:
 * - Each User object = one row in the database
 * - Each field = one column in the database
 * - JPA (with Hibernate) automatically creates the table for you!
 * 
 * ANNOTATIONS EXPLAINED:
 * 
 * @Entity - Tells JPA this is a database table
 * @Table - Specifies the table name
 * @Data - Lombok generates getters, setters, toString, equals, hashCode
 * @Builder - Allows you to create objects like: User.builder().userName("John").build()
 * @NoArgsConstructor - Generates a constructor with no arguments (required by JPA)
 * @AllArgsConstructor - Generates a constructor with all fields
 */
@Entity
@Table(name = "users")  // Table name in database
@Data                   // Lombok: generates getters/setters/toString/equals/hashCode
@Builder                // Lombok: implements Builder pattern
@NoArgsConstructor      // Lombok: creates User() constructor
@AllArgsConstructor     // Lombok: creates User(id, userName, ...) constructor
public class User {

    /**
     * PRIMARY KEY
     * 
     * @Id - Marks this as the primary key
     * @GeneratedValue - MySQL will auto-generate this value
     * 
     * IDENTITY strategy means:
     * - Database uses AUTO_INCREMENT
     * - You don't need to set the ID manually
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * USER NAME
     * 
     * Validation rules:
     * @NotBlank - Cannot be null, empty, or just whitespace
     * @Size - Must be between 3 and 50 characters
     * 
     * @Column annotations:
     * - nullable = false: Database constraint (NOT NULL)
     * - unique = true: No two users can have the same username
     */
    @NotBlank(message = "User name is required")
    @Size(min = 3, max = 50, message = "User name must be between 3 and 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String userName;

    /**
     * EMAIL
     * 
     * @Email - Validates email format (must contain @ and domain)
     * The regex pattern ensures email ends with .com
     * 
     * WHY VALIDATION IS IMPORTANT:
     * - Prevents bad data from entering the database
     * - Provides clear error messages to users
     * - Spring automatically validates before saving
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Pattern(regexp = ".*@.*\\.com$", message = "Email must contain @ and end with .com")
    @Column(nullable = false, unique = true, length = 100)
    private String userEmail;

    /**
     * PASSWORD
     * 
     * SECURITY NOTE FOR BEGINNERS:
     * In a real application, you should:
     * 1. NEVER store passwords as plain text
     * 2. Use BCryptPasswordEncoder to hash passwords
     * 3. Store only the hashed version
     * 
     * Example:
     * String hashedPassword = passwordEncoder.encode(plainPassword);
     * 
     * For this beginner project, we're keeping it simple,
     * but remember: in production, always hash passwords!
     * 
     * @Pattern ensures alphanumeric (letters and numbers)
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$", 
             message = "Password must be alphanumeric (letters and numbers)")
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * TIMESTAMPS
     * 
     * These track when records are created and updated.
     * Very useful for auditing!
     * 
     * @Column(updatable = false) - Cannot be changed after creation
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * JPA LIFECYCLE CALLBACKS
     * 
     * These methods are automatically called by JPA at specific times.
     * Think of them as "hooks" in the lifecycle of an entity.
     * 
     * @PrePersist - Called before saving a NEW entity to database
     * @PreUpdate - Called before UPDATING an existing entity
     * 
     * WHY USE THESE?
     * - Automatically set timestamps
     * - No need to manually set createdAt in your code
     * - Consistent timestamp handling across the application
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
