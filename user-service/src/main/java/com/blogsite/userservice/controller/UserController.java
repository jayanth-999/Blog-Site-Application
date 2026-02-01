package com.blogsite.userservice.controller;

import com.blogsite.userservice.dto.UserRegistrationRequest;
import com.blogsite.userservice.dto.UserResponse;
import com.blogsite.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ========================================
 * USER CONTROLLER - REST API LAYER
 * ========================================
 * 
 * WHAT IS A CONTROLLER?
 * - Handles HTTP requests and responses
 * - Maps URLs to Java methods
 * - First entry point when a client calls your API
 * - Delegates business logic to Service layer
 * 
 * FLOW OF A REQUEST:
 * 1. Client sends HTTP POST to /api/v1.0/blogsite/user/register
 * 2. Spring routes it to registerUser() method
 * 3. Spring validates the request body (@Valid)
 * 4. Method calls UserService to process registration
 * 5. Method wraps response in ResponseEntity
 * 6. Spring converts ResponseEntity to JSON
 * 7. Spring sends HTTP response to client
 * 
 * ANNOTATIONS EXPLAINED:
 * 
 * @RestController
 * - Combination of @Controller and @ResponseBody
 * - Tells Spring this handles REST API requests
 * - Automatically converts return values to JSON
 * 
 * @RequestMapping("/api/v1.0/blogsite/user")
 * - Base path for all endpoints in this controller
 * - All methods in this class will start with this path
 * 
 * @RequiredArgsConstructor (Lombok)
 * - Creates constructor for dependency injection
 * - UserService is injected automatically by Spring
 * 
 * @Slf4j (Lombok)
 * - Provides logging capability
 * - Use log.info(), log.error(), etc.
 * 
 * @Tag (OpenAPI/Swagger)
 * - Groups related endpoints in API documentation
 * - Makes Swagger UI organized and readable
 */
@RestController
@RequestMapping("/api/v1.0/blogsite/user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "APIs for user registration and management")
public class UserController {

    /**
     * DEPENDENCY INJECTION
     * 
     * UserService is injected by Spring.
     * We don't create it with 'new' - Spring manages it!
     */
    private final UserService userService;

    /**
     * ========================================
     * USER REGISTRATION ENDPOINT
     * ========================================
     * 
     * ENDPOINT: POST /api/v1.0/blogsite/user/register
     * 
     * PURPOSE:
     * Register a new user in the system.
     * 
     * REQUEST BODY (JSON):
     * {
     *   "userName": "johndoe",
     *   "userEmail": "john@example.com",
     *   "password": "password123"
     * }
     * 
     * SUCCESSFUL RESPONSE (201 CREATED):
     * {
     *   "id": 1,
     *   "userName": "johndoe",
     *   "userEmail": "john@example.com",
     *   "createdAt": "2024-01-24T10:30:00",
     *   "message": "User registered successfully!"
     * }
     * 
     * ERROR RESPONSES:
     * 400 BAD REQUEST - Validation failed (missing fields, invalid email, etc.)
     * 409 CONFLICT - User already exists with that email/username
     * 500 INTERNAL SERVER ERROR - Unexpected error
     * 
     * ANNOTATIONS EXPLAINED:
     * 
     * @PostMapping("/register")
     * - Handles HTTP POST requests
     * - Full path: /api/v1.0/blogsite/user/register
     * - POST is used for creating new resources
     * 
     * @RequestBody
     * - Maps JSON from request body to Java object
     * - Spring automatically deserializes JSON to UserRegistrationRequest
     * 
     * @Valid
     * - Triggers validation annotations in UserRegistrationRequest
     * - Checks @NotBlank, @Email, @Size, @Pattern
     * - If validation fails, Spring returns 400 Bad Request automatically
     * 
     * @Operation (OpenAPI/Swagger)
     * - Describes what this endpoint does
     * - Shows up in Swagger UI
     * 
     * @ApiResponses (OpenAPI/Swagger)
     * - Documents possible responses
     * - Helps API consumers understand what to expect
     * 
     * ResponseEntity<UserResponse>
     * - Wraps the response
     * - Allows you to set HTTP status code
     * - Returns body + status code + headers
     * 
     * @param request The registration data from client
     * @return ResponseEntity with user details and 201 status
     */
    @PostMapping("/register")
    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account with username, email, and password. " +
                      "Email must contain @ and end with .com. " +
                      "Password must be alphanumeric and at least 8 characters."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "User registered successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data (validation failed)"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "User already exists with this email or username"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody UserRegistrationRequest request) {
        
        // Log the incoming request
        log.info("Received registration request for email: {}", request.getUserEmail());

        // Call service layer to handle business logic
        // Service will:
        // 1. Check if user already exists
        // 2. Create and save user
        // 3. Return response DTO
        UserResponse response = userService.registerUser(request);

        // Return response with 201 CREATED status
        // 201 = Successfully created a new resource
        // The response body contains the created user details
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * ========================================
     * GET USER BY EMAIL (EXAMPLE ENDPOINT)
     * ========================================
     * 
     * ENDPOINT: GET /api/v1.0/blogsite/user/{email}
     * 
     * This is an example of a GET endpoint with path variable.
     * 
     * EXAMPLE:
     * GET /api/v1.0/blogsite/user/john@example.com
     * 
     * @PathVariable
     * - Extracts value from URL path
     * - {email} in URL becomes 'email' parameter
     * 
     * @param email Email address from URL
     * @return User details
     */
    @GetMapping("/{email}")
    @Operation(summary = "Get user by email", description = "Retrieves user information by email address")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        log.info("Fetching user details for email: {}", email);
        UserResponse response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);  // 200 OK
    }

    /**
     * ========================================
     * HEALTH CHECK ENDPOINT
     * ========================================
     * 
     * ENDPOINT: GET /api/v1.0/blogsite/user/health
     * 
     * Simple endpoint to check if the service is running.
     * Useful for monitoring and load balancers.
     * 
     * RESPONSE: "User Service is running!"
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if User Service is running")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("User Service is running!");
    }
}

/*
 * ========================================
 * REST API BEST PRACTICES DEMONSTRATED
 * ========================================
 * 
 * 1. PROPER HTTP METHODS
 *    - POST for creating resources
 *    - GET for retrieving resources
 *    - PUT/PATCH for updating (not shown here)
 *    - DELETE for removing (not shown here)
 * 
 * 2. PROPER HTTP STATUS CODES
 *    - 200 OK - Successful GET
 *    - 201 CREATED - Successfully created resource
 *    - 400 BAD REQUEST - Validation failed
 *    - 404 NOT FOUND - Resource doesn't exist
 *    - 409 CONFLICT - Resource already exists
 *    - 500 INTERNAL SERVER ERROR - Server error
 * 
 * 3. VALIDATION
 *    - Use @Valid to trigger validation
 *    - Validation rules in DTO classes
 *    - Clear error messages
 * 
 * 4. DOCUMENTATION
 *    - OpenAPI/Swagger annotations
 *    - Automatic interactive documentation
 *    - Easy for frontend developers to understand
 * 
 * 5. SEPARATION OF CONCERNS
 *    - Controller handles HTTP
 *    - Service handles business logic
 *    - Repository handles database
 *    - Each layer has one responsibility
 * 
 * 6. LOGGING
 *    - Log important events
 *    - Helps with debugging
 *    - Track API usage
 */
