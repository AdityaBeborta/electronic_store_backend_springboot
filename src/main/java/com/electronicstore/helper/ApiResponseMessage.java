package com.electronicstore.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseMessage {

    private String message;
    private boolean success;
    private HttpStatus status;
}
