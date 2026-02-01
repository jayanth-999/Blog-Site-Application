package com.blogsite.userservice.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprehensive tests for UserResponse DTO including all builder methods
 */
@DisplayName("UserResponse DTO Tests")
class UserResponseTest {

    @Test
    @DisplayName("Should build UserResponse with all fields using builder")
    void testBuilderWithAllFields() {
        LocalDateTime now = LocalDateTime.now();

        UserResponse response = UserResponse.builder()
                .id(1L)
                .userName("johndoe")
                .userEmail("john@example.com")
                .createdAt(now)
                .message("Success")
                .build();

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUserName()).isEqualTo("johndoe");
        assertThat(response.getUserEmail()).isEqualTo("john@example.com");
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getMessage()).isEqualTo("Success");
    }

    @Test
    @DisplayName("Should build UserResponse with partial fields")
    void testBuilderWithPartialFields() {
        UserResponse response = UserResponse.builder()
                .id(1L)
                .userName("johndoe")
                .build();

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUserName()).isEqualTo("johndoe");
        assertThat(response.getUserEmail()).isNull();
    }

    @Test
    @DisplayName("Should test all individual builder methods")
    void testIndividualBuilderMethods() {
        LocalDateTime now = LocalDateTime.now();

        UserResponse.UserResponseBuilder builder = UserResponse.builder();
        builder.id(1L);
        builder.userName("johndoe");
        builder.userEmail("john@example.com");
        builder.createdAt(now);
        builder.message("Success");

        UserResponse response = builder.build();

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();

        UserResponse response1 = UserResponse.builder()
                .id(1L)
                .userName("johndoe")
                .userEmail("john@example.com")
                .createdAt(now)
                .message("Success")
                .build();

        UserResponse response2 = UserResponse.builder()
                .id(1L)
                .userName("johndoe")
                .userEmail("john@example.com")
                .createdAt(now)
                .message("Success")
                .build();

        assertThat(response1).isEqualTo(response2);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    @DisplayName("Should test toString method")
    void testToString() {
        UserResponse response = UserResponse.builder()
                .id(1L)
                .userName("johndoe")
                .build();

        String toString = response.toString();
        assertThat(toString).contains("johndoe");
        assertThat(toString).contains("1");
    }

    @Test
    @DisplayName("Should test setters and getters")
    void testSettersAndGetters() {
        UserResponse response = new UserResponse();
        LocalDateTime now = LocalDateTime.now();

        response.setId(1L);
        response.setUserName("johndoe");
        response.setUserEmail("john@example.com");
        response.setCreatedAt(now);
        response.setMessage("Success");

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUserName()).isEqualTo("johndoe");
        assertThat(response.getUserEmail()).isEqualTo("john@example.com");
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getMessage()).isEqualTo("Success");
    }

    @Test
    @DisplayName("Should create with AllArgsConstructor")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();

        UserResponse response = new UserResponse(1L, "johndoe", "john@example.com", now, "Success");

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getUserName()).isEqualTo("johndoe");
        assertThat(response.getUserEmail()).isEqualTo("john@example.com");
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getMessage()).isEqualTo("Success");
    }

    @Test
    @DisplayName("Should create with NoArgsConstructor")
    void testNoArgsConstructor() {
        UserResponse response = new UserResponse();
        assertThat(response).isNotNull();
    }
}
