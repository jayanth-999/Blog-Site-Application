package com.blogsite.blogservice.dto;

import com.blogsite.blogservice.model.Blog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ========================================
 * BLOG RESPONSE DTO
 * ========================================
 * 
 * This is what the API returns when fetching or creating blogs.
 * 
 * BUILDER PATTERN (Creational Design Pattern):
 * Required by project specifications for composing response objects.
 * 
 * EXAMPLE:
 * BlogResponse response = BlogResponse.builder()
 *     .id(1L)
 *     .blogName("My Amazing Blog Post")
 *     .category("Technology and Innovation")
 *     .build();
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponse {

    private Long id;
    private String blogName;
    private String category;
    private String article;
    private String authorName;
    private LocalDateTime createdAt;
    private String message;  // Optional success message

    /**
     * FACTORY METHOD PATTERN (Creational Design Pattern)
     * 
     * Converts Blog entity to BlogResponse DTO.
     * This encapsulates the conversion logic in one place.
     * 
     * USAGE:
     * Blog blog = blogRepository.findById(1L).get();
     * BlogResponse response = BlogResponse.fromEntity(blog);
     * 
     * @param blog The Blog entity to convert
     * @return BlogResponse DTO
     */
    public static BlogResponse fromEntity(Blog blog) {
        return BlogResponse.builder()
                .id(blog.getId())
                .blogName(blog.getBlogName())
                .category(blog.getCategory())
                .article(blog.getArticle())
                .authorName(blog.getAuthorName())
                .createdAt(blog.getCreatedAt())
                .build();
    }

    /**
     * FACTORY METHOD WITH MESSAGE
     * 
     * Same as fromEntity but with a custom message.
     * 
     * @param blog The Blog entity
     * @param message Success message
     * @return BlogResponse DTO with message
     */
    public static BlogResponse fromEntity(Blog blog, String message) {
        BlogResponse response = fromEntity(blog);
        response.setMessage(message);
        return response;
    }
}
