package com.blogsite.blogservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom exception for blog not found scenarios
 */
@Getter
public class BlogNotFoundException extends RuntimeException {
    
    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public BlogNotFoundException(String message) {
        super(message);
    }
}
