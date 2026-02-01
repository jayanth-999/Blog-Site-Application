package com.blogsite.blogservice.repository;

import com.blogsite.blogservice.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * ========================================
 * BLOG REPOSITORY - DATA ACCESS LAYER
 * ========================================
 * 
 * This repository demonstrates:
 * 1. Spring Data JPA method naming conventions
 * 2. Custom @Query annotations (as required by specification)
 * 3. JPQL (Java Persistence Query Language)
 * 
 * REQUIREMENTS FULFILLED:
 * - Custom queries with @Query
 * - Repository pattern for database access
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    /**
     * ========================================
     * FIND BY CATEGORY (Method Name Query)
     * ========================================
     * 
     * SPRING DATA JPA MAGIC:
     * - Method name: findByCategory
     * - Spring reads: "find" + "By" + "Category"
     * - Generates SQL: SELECT * FROM blogs WHERE category = ?
     * 
     * This is the simplest way to create queries!
     * 
     * @param category The category to search for
     * @return List of all blogs in that category
     */
    List<Blog> findByCategory(String category);

    /**
     * ========================================
     * FIND BY CATEGORY (Custom @Query)
     * ========================================
     * 
     * ALTERNATIVE APPROACH using @Query annotation.
     * Same result as method above, but explicit query.
     * 
     * JPQL EXPLAINED:
     * - Blog (capital B) = Entity name (not table name!)
     * - b = Alias for Blog entity
     * - :category = Named parameter (bound to method parameter)
     * 
     * WHY USE @Query?
     * - Complex queries that can't be expressed in method names
     * - Better performance control
     * - More readable for complex logic
     * 
     * @param category The category to search for
     * @return List of blogs in that category
     */
    @Query("SELECT b FROM Blog b WHERE b.category = :category")
    List<Blog> findBlogsByCategory(@Param("category") String category);

    /**
     * ========================================
     * FIND BY AUTHOR NAME
     * ========================================
     * 
     * Retrieves all blogs created by a specific author.
     * 
     * USAGE:
     * List<Blog> myBlogs = blogRepository.findByAuthorName("johndoe");
     * 
     * @param authorName The author's username
     * @return List of blogs by that author
     */
    List<Blog> findByAuthorName(String authorName);

    /**
     * ========================================
     * FIND BY BLOG NAME
     * ========================================
     * 
     * Find a specific blog by its name.
     * Blog names should be unique per user in a real system.
     * 
     * @param blogName The name of the blog
     * @return Optional containing blog if found
     */
    Optional<Blog> findByBlogName(String blogName);

    /**
     * ========================================
     * FIND BY BLOG NAME AND AUTHOR (Custom @Query)
     * ========================================
     * 
     * More specific search: blog name AND author.
     * This ensures a user can only delete their own blogs.
     * 
     * JPQL WITH MULTIPLE CONDITIONS:
     * - AND combines conditions
     * - Both conditions must be true
     * 
     * @param blogName Name of the blog
     * @param authorName Author's username
     * @return Optional containing blog if found
     */
    @Query("SELECT b FROM Blog b WHERE b.blogName = :blogName AND b.authorName = :authorName")
    Optional<Blog> findByBlogNameAndAuthorName(
            @Param("blogName") String blogName,
            @Param("authorName") String authorName
    );

    /**
     * ========================================
     * FIND BY CATEGORY AND DATE RANGE (Custom @Query)
     * ========================================
     * 
     * COMPLEX QUERY REQUIREMENT:
     * Find blogs in a category within a specific date range.
     * 
     * JPQL FEATURES DEMONSTRATED:
     * - Multiple conditions with AND
     * - Date comparison with BETWEEN
     * - ORDER BY for sorting
     * 
     * SQL EQUIVALENT:
     * SELECT * FROM blogs 
     * WHERE category = ? 
     * AND created_at BETWEEN ? AND ?
     * ORDER BY created_at DESC
     * 
     * EXAMPLE USAGE:
     * LocalDateTime fromDate = LocalDateTime.of(2024, 1, 1, 0, 0);
     * LocalDateTime toDate = LocalDateTime.of(2024, 12, 31, 23, 59);
     * List<Blog> blogs = blogRepository.findByCategoryAndDateRange(
     *     "Technology",
     *     fromDate,
     *     toDate
     * );
     * 
     * @param category The blog category
     * @param fromDate Start of date range
     * @param toDate End of date range
     * @return List of blogs matching criteria, sorted by newest first
     */
    @Query("SELECT b FROM Blog b WHERE b.category = :category " +
           "AND b.createdAt BETWEEN :fromDate AND :toDate " +
           "ORDER BY b.createdAt DESC")
    List<Blog> findByCategoryAndDateRange(
            @Param("category") String category,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    /**
     * ========================================
     * FIND BY DATE RANGE (All Categories)
     * ========================================
     * 
     * Find all blogs within a date range, regardless of category.
     * 
     * @param fromDate Start of date range
     * @param toDate End of date range
     * @return List of blogs in date range
     */
    @Query("SELECT b FROM Blog b WHERE b.createdAt BETWEEN :fromDate AND :toDate " +
           "ORDER BY b.createdAt DESC")
    List<Blog> findByDateRange(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    /**
     * ========================================
     * COUNT BLOGS BY AUTHOR (Custom @Query)
     * ========================================
     * 
     * Count how many blogs an author has created.
     * Useful for statistics or validation.
     * 
     * COUNT QUERY:
     * - Returns a number, not entities
     * - More efficient than fetching all blogs and counting them
     * 
     * @param authorName The author's username
     * @return Number of blogs by this author
     */
    @Query("SELECT COUNT(b) FROM Blog b WHERE b.authorName = :authorName")
    Long countByAuthorName(@Param("authorName") String authorName);

    /**
     * ========================================
     * DELETE BY BLOG NAME AND AUTHOR
     * ========================================
     * 
     * @Modifying and @Query for DELETE operations.
     * 
     * NOTE: In this project, we'll use repository.delete() instead,
     * but this shows how you could do it with @Query.
     * 
     * @param blogName Name of blog to delete
     * @param authorName Author who owns the blog
     */
    // @Modifying
    // @Query("DELETE FROM Blog b WHERE b.blogName = :blogName AND b.authorName = :authorName")
    // void deleteByBlogNameAndAuthor(@Param("blogName") String blogName, @Param("authorName") String authorName);
}

/*
 * ========================================
 * QUERY METHODS RECAP
 * ========================================
 * 
 * SPRING DATA JPA provides 3 ways to create queries:
 * 
 * 1. METHOD NAMING CONVENTION (Easiest)
 *    - findByFieldName()
 *    - findByField1AndField2()
 *    - Spring generates SQL automatically
 * 
 * 2. @Query WITH JPQL (Most flexible)
 *    - Write queries in Java Persistence Query Language
 *    - Works with entity names, not table names
 *    - Database-independent
 * 
 * 3. @Query WITH NATIVE SQL (Advanced)
 *    - Write actual SQL queries
 *    - Database-specific
 *    - Use when JPQL can't do what you need
 *    - Example: @Query(value = "SELECT * FROM blogs WHERE ...", nativeQuery = true)
 * 
 * REQUIREMENTS FULFILLED:
 * ✅ Custom queries with @Query
 * ✅ Aggregations (COUNT)
 * ✅ Date filtering
 * ✅ Multiple conditions
 */
