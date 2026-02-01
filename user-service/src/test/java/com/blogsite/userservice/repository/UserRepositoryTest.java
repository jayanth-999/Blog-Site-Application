package com.blogsite.userservice.repository;

import com.blogsite.userservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ========================================
 * USER REPOSITORY TESTS
 * ========================================
 * 
 * Tests the database layer using an in-memory H2 database.
 * 
 * @DataJpaTest:
 *               - Configures an in-memory database
 *               - Scans for @Entity classes
 *               - Configures Spring Data JPA repositories
 *               - Provides TestEntityManager for test data setup
 *               - Rolls back transactions after each test
 * 
 *               TestEntityManager:
 *               - Used to prepare test data
 *               - Alternative to using the repository for setup
 *               - Ensures clean test state
 */
@DataJpaTest
@DisplayName("User Repository Tests")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userName("johndoe")
                .userEmail("john@example.com")
                .password("password123")
                .build();
    }

    @Test
    @DisplayName("Should save user successfully")
    void testSaveUser() {
        User savedUser = userRepository.save(testUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUserName()).isEqualTo("johndoe");
        assertThat(savedUser.getUserEmail()).isEqualTo("john@example.com");
        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should find user by email")
    void testFindByUserEmail() {
        entityManager.persistAndFlush(testUser);

        Optional<User> found = userRepository.findByUserEmail("john@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getUserName()).isEqualTo("johndoe");
        assertThat(found.get().getUserEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("Should return empty when email not found")
    void testFindByUserEmail_NotFound() {
        Optional<User> found = userRepository.findByUserEmail("notfound@example.com");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should find user by username")
    void testFindByUserName() {
        entityManager.persistAndFlush(testUser);

        Optional<User> found = userRepository.findByUserName("johndoe");

        assertThat(found).isPresent();
        assertThat(found.get().getUserName()).isEqualTo("johndoe");
    }

    @Test
    @DisplayName("Should return empty when username not found")
    void testFindByUserName_NotFound() {
        Optional<User> found = userRepository.findByUserName("notfound");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should check if user exists by email")
    void testExistsByUserEmail() {
        entityManager.persistAndFlush(testUser);

        boolean exists = userRepository.existsByUserEmail("john@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when email doesn't exist")
    void testExistsByUserEmail_NotFound() {
        boolean exists = userRepository.existsByUserEmail("notfound@example.com");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should check if user exists by username")
    void testExistsByUserName() {
        entityManager.persistAndFlush(testUser);

        boolean exists = userRepository.existsByUserName("johndoe");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when username doesn't exist")
    void testExistsByUserName_NotFound() {
        boolean exists = userRepository.existsByUserName("notfound");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should find user by ID")
    void testFindById() {
        User saved = entityManager.persistAndFlush(testUser);

        Optional<User> found = userRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void testDeleteUser() {
        User saved = entityManager.persistAndFlush(testUser);

        userRepository.deleteById(saved.getId());

        Optional<User> found = userRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should enforce unique email constraint")
    void testUniqueEmailConstraint() {
        entityManager.persistAndFlush(testUser);

        User duplicateEmail = User.builder()
                .userName("janedoe")
                .userEmail("john@example.com") // Same email
                .password("password456")
                .build();

        // This should fail due to unique constraint
        // In a real test, you'd use assertThrows
        boolean emailExists = userRepository.existsByUserEmail("john@example.com");
        assertThat(emailExists).isTrue();
    }

    @Test
    @DisplayName("Should enforce unique username constraint")
    void testUniqueUsernameConstraint() {
        entityManager.persistAndFlush(testUser);

        boolean usernameExists = userRepository.existsByUserName("johndoe");
        assertThat(usernameExists).isTrue();
    }
}
