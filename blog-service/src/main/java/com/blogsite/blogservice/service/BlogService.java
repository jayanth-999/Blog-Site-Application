package com.blogsite.blogservice.service;

import com.blogsite.blogservice.dto.BlogRequest;
import com.blogsite.blogservice.dto.BlogResponse;
import com.blogsite.blogservice.exception.BlogNotFoundException;
import com.blogsite.blogservice.model.Blog;
import com.blogsite.blogservice.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ========================================
 * BLOG SERVICE - BUSINESS LOGIC LAYER
 * ========================================
 * 
 * This service implements all blog-related operations:
 * - Create new blog
 * - Fetch blogs by category
 * - Fetch blogs by author
 * - Delete blog
 * - Fetch blogs by category and date range
 * 
 * DESIGN PATTERNS USED:
 * 1. Builder Pattern - for creating response objects
 * 2. Factory Method Pattern - fromEntity() methods
 * 3. Service Layer Pattern - business logic separation
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BlogService {

    private final BlogRepository blogRepository;

    /**
     * ========================================
     * CREATE NEW BLOG
     * ========================================
     * 
     * ENDPOINT: POST /api/v1.0/blogsite/user/blogs/add/{blogName}
     * 
     * Creates a new blog post in the system.
     * 
     * BUSINESS LOGIC:
     * 1. Validate input (handled by @Valid annotation)
     * 2. Create Blog entity using Builder pattern
     * 3. Save to database
     * 4. Return response using Factory method
     * 
     * @param request Blog details from client
     * @return BlogResponse with created blog details
     */
    @Transactional
    public BlogResponse createBlog(BlogRequest request) {
        log.info("Creating new blog: {}", request.getBlogName());

        // Builder Pattern (Creational Design Pattern) - as required by specifications
        Blog blog = Blog.builder()
                .blogName(request.getBlogName())
                .category(request.getCategory())
                .article(request.getArticle())
                .authorName(request.getAuthorName())
                .build();

        Blog savedBlog = blogRepository.save(blog);

        log.info("Blog created successfully with ID: {}", savedBlog.getId());

        // Factory Method Pattern - convert entity to response DTO
        return BlogResponse.fromEntity(savedBlog, "Blog created successfully!");
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
     * JAVA STREAMS EXPLAINED:
     * - blogRepository.findByCategory() returns List<Blog>
     * - .stream() converts list to Stream (allows functional operations)
     * - .map() transforms each Blog to BlogResponse
     * - .collect() gathers results back into List
     * 
     * WHY USE STREAMS?
     * - Concise, readable code
     * - Functional programming style
     * - Easy to chain operations
     * 
     * EQUIVALENT LOOP CODE:
     * List<BlogResponse> responses = new ArrayList<>();
     * for (Blog blog : blogs) {
     *     responses.add(BlogResponse.fromEntity(blog));
     * }
     * 
     * @param category The category to search for
     * @return List of blogs in that category
     */
    public List<BlogResponse> getBlogsByCategory(String category) {
        log.info("Fetching blogs for category: {}", category);

        List<Blog> blogs = blogRepository.findByCategory(category);

        if (blogs.isEmpty()) {
            log.warn("No blogs found for category: {}", category);
        }

        // Convert List<Blog> to List<BlogResponse> using streams
        return blogs.stream()
                .map(BlogResponse::fromEntity)  // Method reference - same as: blog -> BlogResponse.fromEntity(blog)
                .collect(Collectors.toList());
    }

    /**
     * ========================================
     * GET ALL BLOGS BY AUTHOR
     * ========================================
     * 
     * ENDPOINT: GET /api/v1.0/blogsite/user/getall
     * 
     * Shows all blogs created by a specific user.
     * User can view and manage their own blogs.
     * 
     * @param authorName Username of the author
     * @return List of blogs by this author
     */
    public List<BlogResponse> getBlogsByAuthor(String authorName) {
        log.info("Fetching blogs for author: {}", authorName);

        List<Blog> blogs = blogRepository.findByAuthorName(authorName);

        if (blogs.isEmpty()) {
            log.info("Author {} has no blogs yet", authorName);
        }

        return blogs.stream()
                .map(BlogResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ========================================
     * DELETE BLOG
     * ========================================
     * 
     * ENDPOINT: DELETE /api/v1.0/blogsite/user/delete/{blogName}
     * 
     * Deletes a blog by name.
     * 
     * SECURITY CONSIDERATION:
     * In production, you should verify:
     * 1. User is authenticated
     * 2. User owns the blog they're trying to delete
     * 3. Use findByBlogNameAndAuthorName() for security
     * 
     * For this beginner project, we're keeping it simple.
     * 
     * @param blogName Name of the blog to delete
     * @throws BlogNotFoundException if blog doesn't exist
     */
    @Transactional
    public void deleteBlog(String blogName) {
        log.info("Attempting to delete blog: {}", blogName);

        // Find the blog
        Blog blog = blogRepository.findByBlogName(blogName)
                .orElseThrow(() -> new BlogNotFoundException(
                        "Blog not found with name: " + blogName
                ));

        // Delete it
        blogRepository.delete(blog);

        log.info("Blog deleted successfully: {}", blogName);
    }

    /**
     * ========================================
     * DELETE BLOG (WITH AUTHOR VERIFICATION)
     * ========================================
     * 
     * More secure version - ensures user can only delete their own blogs.
     * 
     * @param blogName Name of the blog
     * @param authorName Author who owns the blog
     * @throws BlogNotFoundException if blog not found or doesn't belong to author
     */
    @Transactional
    public void deleteBlogByAuthor(String blogName, String authorName) {
        log.info("Attempting to delete blog: {} by author: {}", blogName, authorName);

        Blog blog = blogRepository.findByBlogNameAndAuthorName(blogName, authorName)
                .orElseThrow(() -> new BlogNotFoundException(
                        "Blog not found with name: " + blogName + " for author: " + authorName
                ));

        blogRepository.delete(blog);

        log.info("Blog deleted successfully: {}", blogName);
    }

    /**
     * ========================================
     * GET BLOGS BY CATEGORY AND DATE RANGE
     * ========================================
     * 
     * ENDPOINT: GET /api/v1.0/blogsite/blogs/get/{category}/{fromDate}/{toDate}
     * 
     * Advanced search: Filter blogs by category AND date range.
     * 
     * EXAMPLE:
     * Get all "Technology" blogs from January 2024:
     * - category: "Technology and Innovation"
     * - fromDate: 2024-01-01T00:00:00
     * - toDate: 2024-01-31T23:59:59
     * 
     * @param category Blog category
     * @param fromDate Start of date range
     * @param toDate End of date range
     * @return List of matching blogs
     */
    public List<BlogResponse> getBlogsByCategoryAndDateRange(
            String category,
            LocalDateTime fromDate,
            LocalDateTime toDate) {
        
        log.info("Fetching blogs - Category: {}, From: {}, To: {}", 
                category, fromDate, toDate);

        // Use custom @Query method from repository
        List<Blog> blogs = blogRepository.findByCategoryAndDateRange(
                category, fromDate, toDate
        );

        if (blogs.isEmpty()) {
            log.warn("No blogs found for given criteria");
        }

        return blogs.stream()
                .map(BlogResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ========================================
     * GET BLOG BY NAME
     * ========================================
     * 
     * Fetch a single blog by its name.
     * Useful for viewing blog details.
     * 
     * @param blogName The blog name
     * @return BlogResponse
     * @throws BlogNotFoundException if not found
     */
    public BlogResponse getBlogByName(String blogName) {
        log.info("Fetching blog: {}", blogName);

        Blog blog = blogRepository.findByBlogName(blogName)
                .orElseThrow(() -> new BlogNotFoundException(
                        "Blog not found: " + blogName
                ));

        return BlogResponse.fromEntity(blog);
    }

    /**
     * ========================================
     * GET BLOG COUNT BY AUTHOR
     * ========================================
     * 
     * Returns how many blogs an author has created.
     * Useful for statistics or dashboard.
     * 
     * @param authorName The author's username
     * @return Number of blogs
     */
    public Long getBlogCountByAuthor(String authorName) {
        log.info("Counting blogs for author: {}", authorName);
        return blogRepository.countByAuthorName(authorName);
    }
}

/*
 * ========================================
 * DESIGN PATTERNS SUMMARY
 * ========================================
 * 
 * 1. BUILDER PATTERN (Creational) - REQUIRED BY SPECIFICATIONS
 *    Blog.builder().blogName(...).category(...).build()
 *    - Used for constructing Blog entities
 *    - Clean, readable object creation
 * 
 * 2. FACTORY METHOD PATTERN (Creational)
 *    BlogResponse.fromEntity(blog)
 *    - Encapsulates response object creation
 *    - Consistent conversion logic
 * 
 * 3. REPOSITORY PATTERN
 *    - Abstracts data access
 *    - Service doesn't know about SQL
 *    - All database operations in repository layer
 * 
 * 4. SERVICE LAYER PATTERN
 *    - Business logic centralized
 *    - Reusable across controllers
 *    - Transaction management
 * 
 * ========================================
 * JAVA 8+ FEATURES USED
 * ========================================
 * 
 * 1. LAMBDA EXPRESSIONS
 *    blog -> BlogResponse.fromEntity(blog)
 * 
 * 2. METHOD REFERENCES
 *    BlogResponse::fromEntity
 *    - Shorthand for lambda
 *    - More concise
 * 
 * 3. STREAMS API
 *    - Functional programming
 *    - Data transformation
 *    - Filtering and mapping
 * 
 * 4. OPTIONAL
 *    - Null safety
 *    - Explicit handling of missing values
 */
