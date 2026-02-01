package com.blogsite.userservice.repository;

import com.blogsite.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ========================================
 * USER REPOSITORY - DATA ACCESS LAYER
 * ========================================
 * 
 * WHAT IS A REPOSITORY?
 * - An interface that handles database operations
 * - You don't write SQL! Spring Data JPA does it for you
 * - Just define method names following naming conventions
 * 
 * HOW IT WORKS:
 * 1. Extend JpaRepository<Entity, ID Type>
 * 2. Define custom query methods if needed
 * 3. Spring implements the interface automatically!
 * 
 * BUILT-IN METHODS (You get these for FREE):
 * - save(user) - INSERT or UPDATE
 * - findById(id) - SELECT by ID
 * - findAll() - SELECT all users
 * - deleteById(id) - DELETE by ID
 * - count() - COUNT users
 * - exists(id) - Check if exists
 * ...and many more!
 * 
 * @Repository annotation:
 * - Marks this as a Data Access Component
 * - Enables exception translation (converts database errors to Spring exceptions)
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * CUSTOM QUERY METHOD #1
     * 
     * Find a user by email address.
     * 
     * HOW SPRING GENERATES THE QUERY:
     * - Method name: findByUserEmail
     * - Spring reads: "find" + "By" + "UserEmail"
     * - Generates SQL: SELECT * FROM users WHERE user_email = ?
     * 
     * NO @Query ANNOTATION NEEDED! Spring is smart!
     * 
     * METHOD NAMING CONVENTION:
     * - findBy[FieldName] - finds by a field
     * - findBy[Field1]And[Field2] - combines conditions with AND
     * - findBy[Field1]Or[Field2] - combines conditions with OR
     * - findBy[Field]OrderBy[OtherField] - adds sorting
     * 
     * RETURN TYPE:
     * Optional<User> - might find a user, might not
     * Why Optional? Because email might not exist in database
     * 
     * HOW TO USE:
     * Optional<User> userOpt = userRepository.findByUserEmail("john@example.com");
     * if (userOpt.isPresent()) {
     *     User user = userOpt.get();
     *     // do something with user
     * }
     * 
     * @param email The email to search for
     * @return Optional containing User if found, empty if not found
     */
    Optional<User> findByUserEmail(String email);

    /**
     * CUSTOM QUERY METHOD #2
     * 
     * Find a user by username.
     * 
     * GENERATED SQL: SELECT * FROM users WHERE user_name = ?
     * 
     * @param userName The username to search for
     * @return Optional containing User if found, empty if not found
     */
    Optional<User> findByUserName(String userName);

    /**
     * CUSTOM QUERY METHOD #3
     * 
     * Check if a user exists with the given email.
     * 
     * GENERATED SQL: SELECT COUNT(*) > 0 FROM users WHERE user_email = ?
     * 
     * This is more efficient than findByUserEmail() when you only need
     * to check existence, not retrieve the full object.
     * 
     * METHOD NAMING CONVENTION:
     * existsBy[FieldName] - returns boolean
     * 
     * HOW TO USE:
     * if (userRepository.existsByUserEmail("john@example.com")) {
     *     // email already taken
     * }
     * 
     * @param email The email to check
     * @return true if user exists with this email, false otherwise
     */
    boolean existsByUserEmail(String email);

    /**
     * CUSTOM QUERY METHOD #4
     * 
     * Check if a user exists with the given username.
     * 
     * GENERATED SQL: SELECT COUNT(*) > 0 FROM users WHERE user_name = ?
     * 
     * @param userName The username to check
     * @return true if user exists with this username, false otherwise
     */
    boolean existsByUserName(String userName);
}

/*
 * MORE EXAMPLES OF SPRING DATA JPA QUERY METHODS:
 * 
 * // Find users by name (case insensitive)
 * List<User> findByUserNameIgnoreCase(String userName);
 * 
 * // Find users created after a certain date
 * List<User> findByCreatedAtAfter(LocalDateTime date);
 * 
 * // Find users with email containing a string
 * List<User> findByUserEmailContaining(String emailPart);
 * 
 * // Complex query with multiple conditions
 * List<User> findByUserNameAndUserEmail(String userName, String email);
 * 
 * // You can also use @Query for complex SQL:
 * @Query("SELECT u FROM User u WHERE u.userName LIKE %:name%")
 * List<User> searchByName(@Param("name") String name);
 */
