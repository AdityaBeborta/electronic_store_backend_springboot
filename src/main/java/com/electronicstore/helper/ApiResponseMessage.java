package com.electronicstore.helper;

import lombok.*;
import org.springframework.http.HttpStatus;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseMessage {

    private String message;
    private boolean success;
    private HttpStatus status;
}
