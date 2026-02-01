package com.blogsite.userservice.service;

import com.blogsite.userservice.dto.UserRegistrationRequest;
import com.blogsite.userservice.dto.UserResponse;
import com.blogsite.userservice.exception.UserAlreadyExistsException;
import com.blogsite.userservice.model.User;
import com.blogsite.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ========================================
 * USER SERVICE - BUSINESS LOGIC LAYER
 * ========================================
 * 
 * WHAT IS A SERVICE?
 * - Contains business logic (the "rules" of your application)
 * - Sits between Controller (handles HTTP) and Repository (handles database)
 * - Performs validation, transformations, and coordinates operations
 * 
 * LAYERED ARCHITECTURE:
 * 
 * Client → Controller → Service → Repository → Database
 *          (HTTP)     (Business)  (Data Access)
 * 
 * WHY SEPARATE LAYERS?
 * - Single Responsibility: Each layer has one job
 * - Testability: Can test business logic without HTTP or database
 * - Reusability: Service methods can be called from multiple controllers
 * - Maintainability: Easy to find and change business rules
 * 
 * ANNOTATIONS EXPLAINED:
 * 
 * @Service
 * - Marks this as a service component
 * - Spring automatically creates an instance (bean)
 * - Enables transaction management
 * 
 * @RequiredArgsConstructor (Lombok)
 * - Automatically creates constructor for 'final' fields
 * - This is DEPENDENCY INJECTION via constructor
 * - Spring will inject UserRepository when creating this service
 * 
 * @Slf4j (Lombok)
 * - Provides a logger instance: log
 * - Use like: log.info("User registered: {}", userName);
 * - Better than System.out.println()!
 * 
 * @Transactional
 * - Makes method execute in a database transaction
 * - If exception occurs, all database changes are rolled back
 * - Ensures data consistency
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    /**
     * DEPENDENCY INJECTION
     * 
     * This field is 'final' - it must be set in constructor.
     * @RequiredArgsConstructor creates:
     * 
     * public UserService(UserRepository userRepository) {
     *     this.userRepository = userRepository;
     * }
     * 
     * Spring finds UserRepository bean and injects it automatically!
     * 
     * WHY DEPENDENCY INJECTION?
     * - No need to use 'new' keyword
     * - Easy to mock in tests
     * - Loose coupling between classes
     * - Spring manages object lifecycle
     */
    private final UserRepository userRepository;

    /**
     * ========================================
     * REGISTER NEW USER
     * ========================================
     * 
     * This method implements the user registration business logic.
     * 
     * STEPS:
     * 1. Check if email already exists
     * 2. Check if username already exists
     * 3. Create User entity from request DTO
     * 4. Save to database
     * 5. Convert entity to response DTO
     * 6. Return response
     * 
     * @Transactional ensures:
     * - If any step fails, no changes are saved
     * - Database operations are atomic (all or nothing)
     * 
     * EXAMPLE USAGE:
     * UserRegistrationRequest request = new UserRegistrationRequest(...);
     * UserResponse response = userService.registerUser(request);
     * 
     * @param request The registration request containing user data
     * @return UserResponse with user details (no password!)
     * @throws UserAlreadyExistsException if email or username already taken
     */
    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        
        // STEP 1: Log the registration attempt
        // Logging is crucial for debugging and monitoring
        log.info("Attempting to register user with email: {}", request.getUserEmail());

        // STEP 2: Check if email already exists
        // We use exists() instead of find() for better performance
        // We only care IF it exists, not WHAT the data is
        if (userRepository.existsByUserEmail(request.getUserEmail())) {
            log.warn("Registration failed: Email already exists: {}", request.getUserEmail());
            throw new UserAlreadyExistsException(
                "A user with email '" + request.getUserEmail() + "' already exists"
            );
        }

        // STEP 3: Check if username already exists
        if (userRepository.existsByUserName(request.getUserName())) {
            log.warn("Registration failed: Username already exists: {}", request.getUserName());
            throw new UserAlreadyExistsException(
                "A user with username '" + request.getUserName() + "' already exists"
            );
        }

        // STEP 4: Create User entity using Builder pattern
        // This is the Creational Design Pattern mentioned in requirements!
        // 
        // BUILDER PATTERN BENEFITS:
        // - Clear and readable
        // - Can set fields in any order
        // - Immutable construction
        User user = User.builder()
                .userName(request.getUserName())
                .userEmail(request.getUserEmail())
                .password(request.getPassword())  // TODO: In production, hash this!
                .build();

        // STEP 5: Save to database
        // save() returns the saved entity with generated ID and timestamps
        User savedUser = userRepository.save(user);
        
        log.info("User registered successfully with ID: {}", savedUser.getId());

        // STEP 6: Convert Entity to Response DTO
        // We use the factory method from UserResponse
        // This ensures password is NOT included in response
        UserResponse response = UserResponse.fromEntity(savedUser);

        return response;
    }

    /**
     * ========================================
     * FIND USER BY EMAIL
     * ========================================
     * 
     * Retrieves user information by email address.
     * This could be used for login or profile lookup.
     * 
     * NOTE: In a real application, you'd have separate methods for
     * authentication that verify passwords securely.
     * 
     * @param email The email to search for
     * @return UserResponse if found
     * @throws RuntimeException if user not found (you can create a custom exception)
     */
    public UserResponse getUserByEmail(String email) {
        log.info("Searching for user with email: {}", email);
        
        // findByUserEmail returns Optional<User>
        // Optional is a container that may or may not hold a value
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return UserResponse.fromEntity(user);
    }
}

/*
 * ========================================
 * DESIGN PATTERNS USED IN THIS CLASS
 * ========================================
 * 
 * 1. BUILDER PATTERN (Creational)
 *    - Used to construct User entity
 *    - Required by project specifications
 *    - Makes object creation clear and flexible
 * 
 * 2. FACTORY METHOD PATTERN (Creational)
 *    - UserResponse.fromEntity(user)
 *    - Encapsulates object creation logic
 *    - Consistent way to create response objects
 * 
 * 3. DEPENDENCY INJECTION PATTERN
 *    - UserRepository injected via constructor
 *    - Spring manages dependencies
 *    - Loose coupling, easy testing
 * 
 * 4. LAYERED ARCHITECTURE PATTERN
 *    - Clear separation: Controller → Service → Repository
 *    - Each layer has specific responsibility
 *    - Industry standard for web applications
 */
