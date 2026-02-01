package com.blogsite.userservice.service;

import com.blogsite.userservice.dto.UserRegistrationRequest;
import com.blogsite.userservice.dto.UserResponse;
import com.blogsite.userservice.exception.UserAlreadyExistsException;
import com.blogsite.userservice.model.User;
import com.blogsite.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * ========================================
 * USER SERVICE UNIT TESTS
 * ========================================
 * 
 * WHAT IS UNIT TESTING?
 * - Testing individual components in isolation
 * - Verify that each method works correctly
 * - Catch bugs early in development
 * - Serve as code documentation
 * 
 * WHY TEST?
 * - Confidence: Know your code works
 * - Regression prevention: Catch breaking changes
 * - Documentation: Tests show how code should be used
 * - Better design: Testable code = good code
 * 
 * TESTING FRAMEWORKS USED:
 * 1. JUnit 5 - Test framework
 * 2. Mockito - Mocking framework
 * 3. AssertJ - Fluent assertions
 * 
 * ANNOTATIONS EXPLAINED:
 * 
 * @ExtendWith(MockitoExtension.class)
 * - Enables Mockito annotations
 * - Allows @Mock and @InjectMocks to work
 * 
 * @Mock
 * - Creates a mock (fake) instance of UserRepository
 * - We control what it returns
 * - No real database calls!
 * 
 * @InjectMocks
 * - Creates UserService and injects mocks into it
 * - UserService gets the mock UserRepository
 * 
 * WHY MOCK?
 * - Unit tests should test ONE thing at a time
 * - We test UserService, not the database
 * - Tests run fast (no database connection)
 * - Tests are reliable (no external dependencies)
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceTest {

    /**
     * MOCK OBJECT
     * 
     * This is a fake UserRepository.
     * We tell it what to return using Mockito methods.
     */
    @Mock
    private UserRepository userRepository;

    /**
     * OBJECT UNDER TEST
     * 
     * This is the real UserService we're testing.
     * It has the mock UserRepository injected.
     */
    @InjectMocks
    private UserService userService;

    // Test data (created before each test)
    private UserRegistrationRequest validRequest;

    /**
     * SETUP METHOD
     * 
     * @BeforeEach runs before EVERY test method.
     * Use it to set up test data and reset mocks.
     * 
     * WHY?
     * - Each test starts with clean state
     * - No test affects another test
     * - Tests can run in any order
     */
    @BeforeEach
    void setUp() {
        // Create a valid registration request
        validRequest = UserRegistrationRequest.builder()
                .userName("testuser")
                .userEmail("test@example.com")
                .password("password123")
                .build();
    }

    /**
     * ========================================
     * TEST: SUCCESSFUL USER REGISTRATION
     * ========================================
     * 
     * WHAT WE'RE TESTING:
     * When registering a new user with valid data,
     * the service should successfully create and return the user.
     * 
     * TEST STRUCTURE (AAA Pattern):
     * 1. ARRANGE - Set up test data and mocks
     * 2. ACT - Call the method being tested
     * 3. ASSERT - Verify the results
     * 
     * @Test marks this as a test method
     * @DisplayName provides a readable test name in reports
     */
    @Test
    @DisplayName("Should successfully register a new user")
    void testRegisterUser_Success() {
        // ARRANGE
        // Tell mocks what to return
        when(userRepository.existsByUserEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUserName(anyString())).thenReturn(false);
        
        // Create the user that will be "saved"
        User savedUser = User.builder()
                .id(1L)
                .userName(validRequest.getUserName())
                .userEmail(validRequest.getUserEmail())
                .password(validRequest.getPassword())
                .build();
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // ACT
        // Call the method we're testing
        UserResponse response = userService.registerUser(validRequest);

        // ASSERT
        // Verify the results using AssertJ
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUserName()).isEqualTo("testuser");
        assertThat(response.getUserEmail()).isEqualTo("test@example.com");
        assertThat(response.getMessage()).contains("successfully");

        // Verify that repository methods were called
        verify(userRepository, times(1)).existsByUserEmail("test@example.com");
        verify(userRepository, times(1)).existsByUserName("testuser");
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * ========================================
     * TEST: DUPLICATE EMAIL
     * ========================================
     * 
     * NEGATIVE TEST CASE
     * 
     * Tests that the service throws an exception
     * when trying to register with an existing email.
     */
    @Test
    @DisplayName("Should throw exception when email already exists")
    void testRegisterUser_EmailExists() {
        // ARRANGE
        // Mock that email already exists
        when(userRepository.existsByUserEmail(anyString())).thenReturn(true);

        // ACT & ASSERT
        // Verify that exception is thrown
        assertThatThrownBy(() -> userService.registerUser(validRequest))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("email");

        // Verify save was NOT called (registration failed)
        verify(userRepository, never()).save(any(User.class));
    }

    /**
     * ========================================
     * TEST: DUPLICATE USERNAME
     * ========================================
     * 
     * Another negative test case.
     */
    @Test
    @DisplayName("Should throw exception when username already exists")
    void testRegisterUser_UsernameExists() {
        // ARRANGE
        when(userRepository.existsByUserEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUserName(anyString())).thenReturn(true);

        // ACT & ASSERT
        assertThatThrownBy(() -> userService.registerUser(validRequest))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("username");

        verify(userRepository, never()).save(any(User.class));
    }
}

/*
 * ========================================
 * TESTING BEST PRACTICES DEMONSTRATED
 * ========================================
 * 
 * 1. UNIT TESTS TEST ONE THING
 *    - Each test has a single purpose
 *    - Clear test names describe what's being tested
 * 
 * 2. AAA PATTERN
 *    - Arrange: Set up test data
 *    - Act: Call the method
 *    - Assert: Verify results
 * 
 * 3. TEST BOTH HAPPY AND UNHAPPY PATHS
 *    - Positive tests: Things work as expected
 *    - Negative tests: Errors are handled correctly
 * 
 * 4. USE MOCKS FOR DEPENDENCIES
 *    - Don't test the database in service tests
 *    - Fast, reliable tests
 * 
 * 5. VERIFY INTERACTIONS
 *    - Check that methods were called
 *    - Check they were called with correct parameters
 * 
 * 6. DESCRIPTIVE TEST NAMES
 *    - testMethodName_Scenario_ExpectedResult
 *    - Or use @DisplayName for natural language
 * 
 * TO RUN TESTS:
 * - From IDE: Right-click test class and select "Run"
 * - From terminal: mvn test
 * - See code coverage: mvn verify (generates JaCoCo report)
 */
