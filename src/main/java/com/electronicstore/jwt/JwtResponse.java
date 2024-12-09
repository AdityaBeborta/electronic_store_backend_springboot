package com.electronicstore.jwt;

import com.electronicstore.dtos.UserDto;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtResponse {
    private String jwtToken;
    private UserDto user;
}
