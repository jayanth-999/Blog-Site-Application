package com.blogsite.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * ========================================
 * GLOBAL EXCEPTION HANDLER
 * ========================================
 * 
 * WHAT IS @RestControllerAdvice?
 * - A global exception handler for all controllers
 * - Intercepts exceptions thrown anywhere in your app
 * - Returns proper error responses to the client
 * 
 * WHY USE IT?
 * Without this:
 * - Clients get ugly stack traces
 * - No consistent error format
 * - Hard to debug from client side
 * 
 * With this:
 * - Clean, user-friendly error messages
 * - Consistent JSON error format
 * - Proper HTTP status codes
 * 
 * HOW IT WORKS:
 * 1. Exception is thrown somewhere in your code
 * 2. Spring catches it
 * 3. Looks for @ExceptionHandler method matching the exception type
 * 4. Calls that method to generate the response
 * 5. Returns the response to the client
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * HANDLE VALIDATION ERRORS
     * 
     * When does this trigger?
     * - When @Valid validation fails (e.g., @NotBlank, @Email, @Size)
     * - Spring throws MethodArgumentNotValidException
     * - This method catches it and formats a nice response
     * 
     * EXAMPLE:
     * Client sends: { "userName": "", "userEmail": "invalid" }
     * Response: {
     *   "timestamp": "2024-01-24T10:30:00",
     *   "status": 400,
     *   "errors": {
     *     "userName": "User name is required",
     *     "userEmail": "Email must be valid"
     *   }
     * }
     * 
     * @param ex The validation exception
     * @return ResponseEntity with validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        // Create a map to hold field-specific errors
        Map<String, String> fieldErrors = new HashMap<>();
        
        // Extract each validation error
        // ex.getBindingResult() contains all validation failures
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            // Get the field name (e.g., "userName", "userEmail")
            String fieldName = ((FieldError) error).getField();
            
            // Get the error message (e.g., "User name is required")
            String errorMessage = error.getDefaultMessage();
            
            // Add to our map
            fieldErrors.put(fieldName, errorMessage);
        });

        // Build the complete error response
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());  // 400
        response.put("error", "Validation Failed");
        response.put("errors", fieldErrors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * HANDLE USER ALREADY EXISTS ERROR
     * 
     * When does this trigger?
     * - When trying to register with existing email or username
     * - Our service throws UserAlreadyExistsException
     * - This method catches it and returns 409 CONFLICT
     * 
     * EXAMPLE:
     * Response: {
     *   "timestamp": "2024-01-24T10:30:00",
     *   "status": 409,
     *   "error": "User Already Exists",
     *   "message": "A user with this email already exists"
     * }
     * 
     * @param ex The custom exception
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExists(
            UserAlreadyExistsException ex) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.CONFLICT.value());  // 409
        response.put("error", "User Already Exists");
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * HANDLE ALL OTHER EXCEPTIONS
     * 
     * This is a catch-all for any unexpected errors.
     * 
     * BEST PRACTICE:
     * - Log the full exception details server-side for debugging
     * - Return a generic message to the client (don't expose internals)
     * 
     * @param ex Any exception not handled by specific handlers
     * @return ResponseEntity with generic error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        
        // Log the full exception server-side
        System.err.println("Unexpected error: " + ex.getMessage());
        ex.printStackTrace();

        // Return generic error to client
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());  // 500
        response.put("error", "Internal Server Error");
        response.put("message", "An unexpected error occurred. Please try again later.");

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
