package com.electronicstore.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDto {

    private String userId;

    private String name;

    private String email;

    private String password;
    private String gender;

    private String about;

    private String imageName;
}
