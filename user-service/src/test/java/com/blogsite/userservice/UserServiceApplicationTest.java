package com.blogsite.userservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * ========================================
 * APPLICATION CONTEXT TESTS
 * ========================================
 * 
 * Verifies that the Spring Boot application context loads successfully.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("User Service Application Tests")
class UserServiceApplicationTest {

    @Test
    @DisplayName("Context should load successfully")
    void contextLoads() {
        // This test verifies that the Spring application context loads without errors
        // It's a basic sanity check that all beans can be created and autowired
        // correctly
    }
}
