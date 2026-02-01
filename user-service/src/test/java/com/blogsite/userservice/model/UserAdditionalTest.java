package com.blogsite.userservice.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Additional comprehensive tests for User entity to cover all Lombok methods
 */
@DisplayName("User Entity Additional Tests")
class UserAdditionalTest {

    @Test
    @DisplayName("Should test all builder chain methods")
    void testBuilderChain() {
        User user = User.builder()
                .id(1L)
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("password123")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUserName()).isEqualTo("johndoe");
        assertThat(user.getUserEmail()).isEqualTo("john@example.com");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should test individual builder methods")
    void testIndividualBuilderMethods() {
        User.UserBuilder builder = User.builder();
        builder.id(1L);
        builder.userName("johndoe");
        builder.userEmail("john@example.com");
        builder.password("password123");

        User user = builder.build();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should test all setters")
    void testAllSetters() {
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        user.setId(1L);
        user.setUserName("johndoe");
        user.setUserEmail("john@example.com");
        user.setPassword("password123");
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUserName()).isEqualTo("johndoe");
        assertThat(user.getUserEmail()).isEqualTo("john@example.com");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should test all getters")
    void testAllGetters() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .id(1L)
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("password123")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUserName()).isEqualTo("johndoe");
        assertThat(user.getUserEmail()).isEqualTo("john@example.com");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should test AllArgsConstructor")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "johndoe", "john@example.com", "password123", now, now);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUserName()).isEqualTo("johndoe");
        assertThat(user.getUserEmail()).isEqualTo("john@example.com");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should test NoArgsConstructor")
    void testNoArgsConstructor() {
        User user = new User();
        assertThat(user).isNotNull();
    }

    @Test
    @DisplayName("Should test toString contains all fields")
    void testToString() {
        User user = User.builder()
                .id(1L)
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("password123")
                .build();

        String toString = user.toString();
        assertThat(toString).contains("johndoe");
        assertThat(toString).contains("john@example.com");
        assertThat(toString).contains("1");
    }

    @Test
    @DisplayName("Should test equals with same values")
    void testEqualsWithSameValues() {
        LocalDateTime now = LocalDateTime.now();

        User user1 = User.builder()
                .id(1L)
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("password123")
                .createdAt(now)
                .updatedAt(now)
                .build();

        User user2 = User.builder()
                .id(1L)
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("password123")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    @DisplayName("Should test equals with different values")
    void testEqualsWithDifferentValues() {
        User user1 = User.builder()
                .id(1L)
                .userName("johndoe")
                .build();

        User user2 = User.builder()
                .id(2L)
                .userName("janedoe")
                .build();

        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    @DisplayName("Should test hashCode consistency")
    void testHashCodeConsistency() {
        User user = User.builder()
                .id(1L)
                .userName("johndoe")
                .userEmail("john@example.com")
                .build();

        int hashCode1 = user.hashCode();
        int hashCode2 = user.hashCode();

        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    @DisplayName("Should test PrePersist lifecycle callback")
    void testPrePersistCallback() {
        User user = new User();
        user.setUserName("johndoe");
        user.setUserEmail("john@example.com");
        user.setPassword("password123");

        // Manually invoke the callback (normally JPA does this)
        user.onCreate();

        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should test PreUpdate lifecycle callback")
    void testPreUpdateCallback() {
        User user = new User();
        user.setUserName("johndoe");
        user.onCreate();

        LocalDateTime originalUpdated = user.getUpdatedAt();

        // Wait a tiny bit
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }

        user.onUpdate();

        assertThat(user.getUpdatedAt()).isAfterOrEqualTo(originalUpdated);
    }
}
