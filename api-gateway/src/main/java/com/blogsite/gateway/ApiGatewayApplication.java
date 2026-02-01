package com.blogsite.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ========================================
 * API GATEWAY - MAIN APPLICATION CLASS
 * ========================================
 * 
 * WHAT IS AN API GATEWAY?
 * Think of it as a "front door" to your microservices.
 * All client requests come here first, then get routed
 * to the appropriate service.
 * 
 * CLIENT → API GATEWAY → User Service
 *                      → Blog Service
 * 
 * BENEFITS:
 * 1. Single URL for clients (http://localhost:8080)
 * 2. Clients don't need to know about individual services
 * 3. Easy to add security, logging, rate limiting
 * 4. Load balancing (if you have multiple instances)
 * 
 * ROUTING EXAMPLE:
 * - Client calls: POST http://localhost:8080/api/v1.0/blogsite/user/register
 * - Gateway routes to: User Service at http://localhost:8081/api/v1.0/blogsite/user/register
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
        
        System.out.println("========================================");
        System.out.println("✅ API Gateway Started Successfully!");
        System.out.println("🌐 Gateway URL: http://localhost:8080");
        System.out.println("📡 Routing to User Service (8081) and Blog Service (8082)");
        System.out.println("========================================");
    }
}
