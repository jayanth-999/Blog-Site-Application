package com.blogsite.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ========================================
 * USER SERVICE - MAIN APPLICATION CLASS
 * ========================================
 * 
 * This is the entry point of the User Service microservice.
 * 
 * WHAT DOES THIS CLASS DO?
 * - Starts the Spring Boot application
 * - Configures auto-configuration (Spring Boot magic!)
 * - Scans for components (controllers, services, repositories)
 * 
 * ANNOTATIONS EXPLAINED:
 * 
 * @SpringBootApplication
 * This is a convenience annotation that combines three annotations:
 * 1. @Configuration - Marks this class as a source of bean definitions
 * 2. @EnableAutoConfiguration - Tells Spring Boot to automatically configure your app
 * 3. @ComponentScan - Tells Spring to scan for components in this package and sub-packages
 * 
 * HOW TO RUN:
 * - From IDE: Right-click and select "Run"
 * - From terminal: mvn spring-boot:run
 * - The service will start on port 8081 (configured in application.yml)
 */
@SpringBootApplication
public class UserServiceApplication {

    /**
     * Main method - the starting point of any Java application
     * 
     * @param args Command line arguments (usually not used in Spring Boot)
     */
    public static void main(String[] args) {
        // SpringApplication.run() does the following:
        // 1. Creates an application context (Spring container)
        // 2. Loads all beans (components, services, controllers, repositories)
        // 3. Starts the embedded Tomcat server
        // 4. Makes your REST APIs available
        SpringApplication.run(UserServiceApplication.class, args);
        
        System.out.println("========================================");
        System.out.println("✅ User Service Started Successfully!");
        System.out.println("🌐 Swagger UI: http://localhost:8081/swagger-ui.html");
        System.out.println("📚 API Docs: http://localhost:8081/v3/api-docs");
        System.out.println("========================================");
    }
}
