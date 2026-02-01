package com.blogsite.userservice.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ========================================
 * USER ENTITY VALIDATION TESTS
 * ========================================
 * 
 * Tests the validation annotations on the User entity.
 */
@DisplayName("User Entity Tests")
class UserTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("Should create valid user")
    void testValidUser() {
        User user = User.builder()
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("password123")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail when username is blank")
    void testBlankUserName() {
        User user = User.builder()
                .userName("")
                .userEmail("john@example.com")
                .password("password123")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("userName"));
    }

    @Test
    @DisplayName("Should fail when email is invalid")
    void testInvalidEmail() {
        User user = User.builder()
                .userName("johndoe")
                .userEmail("invalid-email")
                .password("password123")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("Should fail when password is too short")
    void testShortPassword() {
        User user = User.builder()
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("pass12")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    @DisplayName("Should test builder pattern")
    void testBuilderPattern() {
        User user = User.builder()
                .id(1L)
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("password123")
                .build();

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUserName()).isEqualTo("johndoe");
        assertThat(user.getUserEmail()).isEqualTo("john@example.com");
        assertThat(user.getPassword()).isEqualTo("password123");
    }

    @Test
    @DisplayName("Should test equals and hashcode")
    void testEqualsAndHashCode() {
        User user1 = User.builder()
                .id(1L)
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("password123")
                .build();

        User user2 = User.builder()
                .id(1L)
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("password123")
                .build();

        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }
}
