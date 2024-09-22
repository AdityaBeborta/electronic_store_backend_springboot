package com.electronicstore.exceptions;

import com.electronicstore.helper.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //Handle ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponseMessage build = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.NOT_FOUND).success(true).build();
        logger.error("Resource Not Found exception {} ", build);
        return new ResponseEntity<>(build, HttpStatus.NOT_FOUND);
    }

    //Handle MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        logger.warn("MethodArgumentNotValidException {}",allErrors);
        Map<String, String> response = new HashMap<>();
        allErrors.stream().forEach(objectError -> {
            String defaultMessage = objectError.getDefaultMessage();
            //typecast objectError to field error to get the particular filed
            String field = ((FieldError) objectError).getField();
            response.put(field,defaultMessage);
        });
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

}
