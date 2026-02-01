package com.blogsite.userservice.controller;

import com.blogsite.userservice.dto.UserRegistrationRequest;
import com.blogsite.userservice.dto.UserResponse;
import com.blogsite.userservice.exception.UserAlreadyExistsException;
import com.blogsite.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ========================================
 * USER CONTROLLER INTEGRATION TESTS
 * ========================================
 * 
 * These tests verify the REST API endpoints work correctly.
 * 
 * @WebMvcTest:
 *              - Loads only the web layer (controllers)
 *              - Doesn't load the full application context
 *              - Perfect for testing REST endpoints
 * 
 *              MockMvc:
 *              - Simulates HTTP requests without starting a server
 *              - Tests your controller methods
 *              - Verifies request/response handling
 */
@WebMvcTest(UserController.class)
@DisplayName("User Controller Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserRegistrationRequest validRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        validRequest = UserRegistrationRequest.builder()
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("password123")
                .build();

        userResponse = UserResponse.builder()
                .id(1L)
                .userName("johndoe")
                .userEmail("john@example.com")
                .createdAt(LocalDateTime.now())
                .message("User registered successfully!")
                .build();
    }

    @Test
    @DisplayName("Should register user successfully")
    void testRegisterUser_Success() throws Exception {
        // Arrange
        when(userService.registerUser(any(UserRegistrationRequest.class)))
                .thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1.0/blogsite/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userName").value("johndoe"))
                .andExpect(jsonPath("$.userEmail").value("john@example.com"))
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        verify(userService, times(1)).registerUser(any(UserRegistrationRequest.class));
    }

    @Test
    @DisplayName("Should return 400 when username is blank")
    void testRegisterUser_BlankUsername() throws Exception {
        UserRegistrationRequest invalidRequest = UserRegistrationRequest.builder()
                .userName("")
                .userEmail("john@example.com")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/v1.0/blogsite/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.userName").exists());

        verify(userService, never()).registerUser(any());
    }

    @Test
    @DisplayName("Should return 400 when username is too short")
    void testRegisterUser_ShortUsername() throws Exception {
        UserRegistrationRequest invalidRequest = UserRegistrationRequest.builder()
                .userName("ab")
                .userEmail("john@example.com")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/v1.0/blogsite/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.userName").exists());
    }

    @Test
    @DisplayName("Should return 400 when email is invalid")
    void testRegisterUser_InvalidEmail() throws Exception {
        UserRegistrationRequest invalidRequest = UserRegistrationRequest.builder()
                .userName("johndoe")
                .userEmail("invalid-email")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/v1.0/blogsite/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.userEmail").exists());
    }

    @Test
    @DisplayName("Should return 400 when email doesn't end with .com")
    void testRegisterUser_EmailWithoutDotCom() throws Exception {
        UserRegistrationRequest invalidRequest = UserRegistrationRequest.builder()
                .userName("johndoe")
                .userEmail("john@example.org")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/v1.0/blogsite/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.userEmail").exists());
    }

    @Test
    @DisplayName("Should return 400 when password is too short")
    void testRegisterUser_ShortPassword() throws Exception {
        UserRegistrationRequest invalidRequest = UserRegistrationRequest.builder()
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("pass12")
                .build();

        mockMvc.perform(post("/api/v1.0/blogsite/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.password").exists());
    }

    @Test
    @DisplayName("Should return 400 when password is not alphanumeric")
    void testRegisterUser_NonAlphanumericPassword() throws Exception {
        UserRegistrationRequest invalidRequest = UserRegistrationRequest.builder()
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("password!!!!")
                .build();

        mockMvc.perform(post("/api/v1.0/blogsite/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.password").exists());
    }

    @Test
    @DisplayName("Should return 409 when user already exists")
    void testRegisterUser_UserAlreadyExists() throws Exception {
        when(userService.registerUser(any(UserRegistrationRequest.class)))
                .thenThrow(new UserAlreadyExistsException("User already exists"));

        mockMvc.perform(post("/api/v1.0/blogsite/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("User Already Exists"))
                .andExpect(jsonPath("$.message").value("User already exists"));
    }

    @Test
    @DisplayName("Should get user by email successfully")
    void testGetUserByEmail_Success() throws Exception {
        when(userService.getUserByEmail("john@example.com"))
                .thenReturn(userResponse);

        mockMvc.perform(get("/api/v1.0/blogsite/user/john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userEmail").value("john@example.com"))
                .andExpect(jsonPath("$.userName").value("johndoe"));

        verify(userService, times(1)).getUserByEmail("john@example.com");
    }

    @Test
    @DisplayName("Should return health check message")
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/v1.0/blogsite/user/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("User Service is running!"));
    }
}
