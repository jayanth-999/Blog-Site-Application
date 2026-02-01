package com.blogsite.blogservice.controller;

import com.blogsite.blogservice.dto.BlogRequest;
import com.blogsite.blogservice.dto.BlogResponse;
import com.blogsite.blogservice.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ========================================
 * BLOG CONTROLLER - REST API LAYER
 * ========================================
 * 
 * This controller exposes all blog-related REST endpoints
 * as specified in the project requirements.
 * 
 * BASE PATH: /api/v1.0/blogsite
 * 
 * ENDPOINTS:
 * 1. POST   /user/blogs/add/{blogName} - Create blog
 * 2. GET    /blogs/info/{category} - Get blogs by category
 * 3. GET    /user/getall - Get all blogs by user
 * 4. DELETE /user/delete/{blogName} - Delete blog
 * 5. GET    /blogs/get/{category}/{fromDate}/{toDate} - Get blogs by date range
 */
@RestController
@RequestMapping("/api/v1.0/blogsite")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Blog Management", description = "APIs for managing blog posts")
public class BlogController {

    private final BlogService blogService;

    /**
     * ========================================
     * CREATE NEW BLOG
     * ========================================
     * 
     * ENDPOINT: POST /api/v1.0/blogsite/user/blogs/add/{blogName}
     * 
     * NOTE: The {blogName} in the path is redundant since it's also in the request body,
     * but we're following the exact API specification provided.
     * 
     * REQUEST BODY:
     * {
     *   "blogName": "Understanding Microservices Architecture",
     *   "category": "Technology and Innovation in Software",
     *   "article": "[1000+ character article content]",
     *   "authorName": "johndoe"
     * }
     * 
     * RESPONSE (201 CREATED):
     * {
     *   "id": 1,
     *   "blogName": "Understanding Microservices Architecture",
     *   "category": "Technology and Innovation in Software",
     *   "article": "...",
     *   "authorName": "johndoe",
     *   "createdAt": "2024-01-24T10:30:00",
     *   "message": "Blog created successfully!"
     * }
     */
    @PostMapping("/user/blogs/add/{blogName}")
    @Operation(
        summary = "Create a new blog post",
        description = "Creates a new blog with specified details. " +
                      "Blog name and category must be at least 20 characters. " +
                      "Article must be at least 1000 characters."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Blog created successfully"),
        @ApiResponse(responseCode = "400", description = "Validation failed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BlogResponse> createBlog(
            @Parameter(description = "Blog name") @PathVariable String blogName,
            @Valid @RequestBody BlogRequest request) {
        
        log.info("Creating blog: {}", blogName);

        // NOTE: For a cleaner API, we could remove blogName from path
        // and just use request.getBlogName()
        BlogResponse response = blogService.createBlog(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * ========================================
     * GET BLOGS BY CATEGORY
     * ========================================
     * 
     * ENDPOINT: GET /api/v1.0/blogsite/blogs/info/{category}
     * 
     * Retrieves all blogs in a specific category.
     * 
     * EXAMPLE:
     * GET /api/v1.0/blogsite/blogs/info/Technology and Innovation
     * 
     * NOTE: URL encoding will happen automatically
     * "Technology and Innovation" → "Technology%20and%20Innovation"
     * 
     * RESPONSE (200 OK):
     * [
     *   {
     *     "id": 1,
     *     "blogName": "Microservices Guide",
     *     "category": "Technology and Innovation",
     *     ...
     *   },
     *   {
     *     "id": 2,
     *     "blogName": "Cloud Computing Basics",
     *     "category": "Technology and Innovation",
     *     ...
     *   }
     * ]
     */
    @GetMapping("/blogs/info/{category}")
    @Operation(
        summary = "Get blogs by category",
        description = "Retrieves all blogs in the specified category"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Blogs retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "No blogs found in category")
    })
    public ResponseEntity<List<BlogResponse>> getBlogsByCategory(
            @Parameter(description = "Blog category") @PathVariable String category) {
        
        log.info("Fetching blogs for category: {}", category);

        List<BlogResponse> blogs = blogService.getBlogsByCategory(category);

        return ResponseEntity.ok(blogs);
    }

    /**
     * ========================================
     * GET ALL BLOGS BY USER
     * ========================================
     * 
     * ENDPOINT: GET /api/v1.0/blogsite/user/getall
     * 
     * Returns all blogs created by a specific user.
     * 
     * NOTE: In a real application, you'd get the username from
     * the authenticated session/JWT token. For this beginner project,
     * we're using a query parameter.
     * 
     * EXAMPLE:
     * GET /api/v1.0/blogsite/user/getall?authorName=johndoe
     * 
     * @RequestParam annotation:
     * - Extracts value from query string
     * - ?authorName=johndoe → authorName parameter
     * - required = true means parameter is mandatory
     */
    @GetMapping("/user/getall")
    @Operation(
        summary = "Get all blogs by author",
        description = "Retrieves all blogs created by a specific user"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Blogs retrieved successfully")
    })
    public ResponseEntity<List<BlogResponse>> getBlogsByAuthor(
            @Parameter(description = "Author's username") 
            @RequestParam(required = true) String authorName) {
        
        log.info("Fetching blogs for author: {}", authorName);

        List<BlogResponse> blogs = blogService.getBlogsByAuthor(authorName);

        return ResponseEntity.ok(blogs);
    }

    /**
     * ========================================
     * DELETE BLOG
     * ========================================
     * 
     * ENDPOINT: DELETE /api/v1.0/blogsite/user/delete/{blogName}
     * 
     * Deletes a blog by its name.
     * 
     * EXAMPLE:
     * DELETE /api/v1.0/blogsite/user/delete/Understanding%20Microservices
     * 
     * RESPONSE (204 NO CONTENT):
     * - No body returned
     * - 204 status means "successfully deleted, no content to return"
     * 
     * ALTERNATIVE RESPONSE (200 OK with message):
     * {
     *   "message": "Blog deleted successfully"
     * }
     */
    @DeleteMapping("/user/delete/{blogName}")
    @Operation(
        summary = "Delete a blog",
        description = "Deletes a blog by its name"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Blog deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Blog not found")
    })
    public ResponseEntity<Void> deleteBlog(
            @Parameter(description = "Name of blog to delete") @PathVariable String blogName) {
        
        log.info("Deleting blog: {}", blogName);

        blogService.deleteBlog(blogName);

        // 204 NO CONTENT - successful deletion with no response body
        return ResponseEntity.noContent().build();
    }

    /**
     * ========================================
     * GET BLOGS BY CATEGORY AND DATE RANGE
     * ========================================
     * 
     * ENDPOINT: GET /api/v1.0/blogsite/blogs/get/{category}/{fromDate}/{toDate}
     * 
     * Advanced filtering: Get blogs by category within a date range.
     * 
     * DATE FORMAT: ISO 8601
     * - Format: yyyy-MM-ddTHH:mm:ss
     * - Example: 2024-01-01T00:00:00
     * 
     * FULL EXAMPLE:
     * GET /api/v1.0/blogsite/blogs/get/Technology/2024-01-01T00:00:00/2024-01-31T23:59:59
     * 
     * This returns all "Technology" blogs created in January 2024.
     * 
     * @DateTimeFormat annotation:
     * - Tells Spring how to parse the date from URL
     * - iso = DateTimeFormat.ISO.DATE_TIME means ISO 8601 format
     * - Without this, Spring wouldn't know how to convert string to LocalDateTime
     */
    @GetMapping("/blogs/get/{category}/{fromDate}/{toDate}")
    @Operation(
        summary = "Get blogs by category and date range",
        description = "Retrieves blogs in a category within the specified date range. " +
                      "Dates should be in ISO format: yyyy-MM-ddTHH:mm:ss"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Blogs retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid date format")
    })
    public ResponseEntity<List<BlogResponse>> getBlogsByCategoryAndDateRange(
            @Parameter(description = "Blog category") 
            @PathVariable String category,
            
            @Parameter(description = "Start date (ISO format: yyyy-MM-ddTHH:mm:ss)", 
                      example = "2024-01-01T00:00:00")
            @PathVariable 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
            LocalDateTime fromDate,
            
            @Parameter(description = "End date (ISO format: yyyy-MM-ddTHH:mm:ss)", 
                      example = "2024-12-31T23:59:59")
            @PathVariable 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
            LocalDateTime toDate) {
        
        log.info("Fetching blogs - Category: {}, From: {}, To: {}", category, fromDate, toDate);

        List<BlogResponse> blogs = blogService.getBlogsByCategoryAndDateRange(
                category, fromDate, toDate
        );

       return ResponseEntity.ok(blogs);
    }

    /**
     * ========================================
     * HEALTH CHECK
     * ========================================
     * 
     * Simple endpoint to verify the service is running.
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if Blog Service is running")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Blog Service is running!");
    }
}

/*
 * ========================================
 * HTTP METHODS EXPLAINED
 * ========================================
 * 
 * GET - Retrieve data (read-only, no side effects)
 *   - Safe: Calling multiple times has same effect
 *   - Cacheable: Responses can be cached
 *   - Example: Get blogs, search
 * 
 * POST - Create new resource
 *   - Not safe: Creates new data
 *   - Not idempotent: Calling twice creates two resources
 *   - Example: Create blog
 * 
 * PUT - Update entire resource (replace)
 *   - Idempotent: Calling multiple times same result
 *   - Example: Update entire blog
 * 
 * PATCH - Update part of resource
 *   - Updates specific fields
 *   - Example: Update just blog title
 * 
 * DELETE - Remove resource
 *   - Idempotent: Deleting twice = same result
 *   - Example: Delete blog
 * 
 * ========================================
 * HTTP STATUS CODES USED
 * ========================================
 * 
 * 200 OK - Successful GET/UPDATE
 * 201 CREATED - Successfully created resource
 * 204 NO CONTENT - Successfully deleted (no body)
 * 400 BAD REQUEST - Validation failed
 * 404 NOT FOUND - Resource doesn't exist
 * 500 INTERNAL SERVER ERROR - Server error
 * 
 * ========================================
 * PATH VARIABLES VS QUERY PARAMETERS
 * ========================================
 * 
 * PATH VARIABLE (@PathVariable):
 * - Part of the URL path
 * - Example: /blogs/delete/{blogName}
 * - Use for: Identifying a specific resource
 * 
 * QUERY PARAMETER (@RequestParam):
 * - After ? in URL
 * - Example: /blogs?category=Tech&author=John
 * - Use for: Filtering, sorting, pagination
 * 
 * REQUEST BODY (@RequestBody):
 * - JSON in POST/PUT requests
 * - Use for: Creating/updating resources with multiple fields
 */
