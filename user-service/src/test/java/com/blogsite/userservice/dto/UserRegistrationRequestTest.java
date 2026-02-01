package com.blogsite.userservice.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprehensive tests for UserRegistrationRequest DTO
 */
@DisplayName("UserRegistrationRequest DTO Tests")
class UserRegistrationRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("Should build valid request with all fields")
    void testBuilderWithAllFields() {
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("password123")
                .build();

        assertThat(request.getUserName()).isEqualTo("johndoe");
        assertThat(request.getUserEmail()).isEqualTo("john@example.com");
        assertThat(request.getPassword()).isEqualTo("password123");
    }

    @Test
    @DisplayName("Should test individual builder methods")
    void testIndividualBuilderMethods() {
        UserRegistrationRequest.UserRegistrationRequestBuilder builder = UserRegistrationRequest.builder();
        builder.userName("johndoe");
        builder.userEmail("john@example.com");
        builder.password("password123");

        UserRegistrationRequest request = builder.build();

        assertThat(request).isNotNull();
        assertThat(request.getUserName()).isEqualTo("johndoe");
    }

    @Test
    @DisplayName("Should test setters and getters")
    void testSettersAndGetters() {
        UserRegistrationRequest request = new UserRegistrationRequest();

        request.setUserName("johndoe");
        request.setUserEmail("john@example.com");
        request.setPassword("password123");

        assertThat(request.getUserName()).isEqualTo("johndoe");
        assertThat(request.getUserEmail()).isEqualTo("john@example.com");
        assertThat(request.getPassword()).isEqualTo("password123");
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void testEqualsAndHashCode() {
        UserRegistrationRequest request1 = UserRegistrationRequest.builder()
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("password123")
                .build();

        UserRegistrationRequest request2 = UserRegistrationRequest.builder()
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("password123")
                .build();

        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
    }

    @Test
    @DisplayName("Should test toString method")
    void testToString() {
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("password123")
                .build();

        String toString = request.toString();
        assertThat(toString).contains("johndoe");
        assertThat(toString).contains("john@example.com");
    }

    @Test
    @DisplayName("Should create with AllArgsConstructor")
    void testAllArgsConstructor() {
        UserRegistrationRequest request = new UserRegistrationRequest("johndoe", "john@example.com", "password123");

        assertThat(request.getUserName()).isEqualTo("johndoe");
        assertThat(request.getUserEmail()).isEqualTo("john@example.com");
        assertThat(request.getPassword()).isEqualTo("password123");
    }

    @Test
    @DisplayName("Should create with NoArgsConstructor")
    void testNoArgsConstructor() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        assertThat(request).isNotNull();
    }

    @Test
    @DisplayName("Should validate username is not blank")
    void testUsernameNotBlank() {
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                .userName("")
                .userEmail("john@example.com")
                .password("password123")
                .build();

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("Should validate email format")
    void testEmailValidation() {
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                .userName("johndoe")
                .userEmail("invalid-email")
                .password("password123")
                .build();

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("Should validate password length")
    void testPasswordLength() {
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("short")
                .build();

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);
        assertThat(violations).isNotEmpty();
    }
}
