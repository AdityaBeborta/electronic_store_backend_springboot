package com.electronicstore.exceptions;

import com.electronicstore.helper.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


import java.io.FileNotFoundException;
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

    //Handle method Argument mismatch exception

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseMessage> handleMethodArgumentException(MethodArgumentTypeMismatchException ex){
        logger.error("MethodArgumentTypeMismatchException {} ",ex.getMessage());
        ApiResponseMessage apiRes = ApiResponseMessage.builder().message(ex.getMessage()).success(true).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(apiRes,HttpStatus.BAD_REQUEST);
    }

    //Handle method property reference exception

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ApiResponseMessage> handleMethodArgumentException(PropertyReferenceException ex){
        logger.error("PropertyReferenceException {} ",ex.getMessage());
        ApiResponseMessage apiRes = ApiResponseMessage.builder().message(ex.getMessage()).success(true).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(apiRes,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseMessage> badApiRequestHandler(BadApiRequest ex){
        logger.error("BadApiRequest {} ",ex.getMessage());
        ApiResponseMessage apiRes = ApiResponseMessage.builder().message(ex.getMessage()).success(false).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(apiRes,HttpStatus.BAD_REQUEST);
    }

    //exception to handle files size too large exception
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponseMessage> fileSizeTooLargeException(MaxUploadSizeExceededException ex){
        logger.error("MaxUploadSizeExceededException {} ",ex.getMessage());
        ApiResponseMessage apiRes = ApiResponseMessage.builder().message(ex.getMessage()).success(false).status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(apiRes,HttpStatus.BAD_REQUEST);
    }

    //handle exception for File not found
    //exception to handle files size too large exception
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> fileNotFoundException(FileNotFoundException ex){
        logger.error("MaxUploadSizeExceededException {} ",ex.getMessage());
        ApiResponseMessage apiRes = ApiResponseMessage.builder().message(ex.getMessage()).success(false).status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(apiRes,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ApiResponseMessage> handleResourceAlreadyExistException(ResourceAlreadyExistException ex) {
        ApiResponseMessage build = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.BAD_REQUEST).success(true).build();
        logger.error("Resource Already exist exception {} ", build);
        return new ResponseEntity<>(build, HttpStatus.BAD_REQUEST);
    }


}
