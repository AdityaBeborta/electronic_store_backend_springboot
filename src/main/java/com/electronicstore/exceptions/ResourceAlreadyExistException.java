package com.electronicstore.exceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;
@Builder
public class ResourceAlreadyExistException extends RuntimeException{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    String resourceName;
    String fieldName;
    String fieldValue;

    public ResourceAlreadyExistException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s already exist with %s : %s", resourceName, fieldName, fieldValue));
    }
}
