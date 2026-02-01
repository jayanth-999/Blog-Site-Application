package com.blogsite.blogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ========================================
 * BLOG SERVICE - MAIN APPLICATION CLASS
 * ========================================
 * 
 * This is the entry point of the Blog Service microservice.
 * 
 * RESPONSIBILITIES:
 * - Add new blogs
 * - Fetch blogs by category
 * - Fetch blogs created by specific user
 * - Delete blogs
 * - Filter blogs by date range
 */
@SpringBootApplication
public class BlogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogServiceApplication.class, args);
        
        System.out.println("========================================");
        System.out.println("✅ Blog Service Started Successfully!");
        System.out.println("🌐 Swagger UI: http://localhost:8082/swagger-ui.html");
        System.out.println("📚 API Docs: http://localhost:8082/v3/api-docs");
        System.out.println("========================================");
    }
}
