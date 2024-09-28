package com.electronicstore.dtos;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDto {

    private String userId;

    @Size(min = 3, max = 15, message = "Name cannot be more than 15 char and less than 3 char")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "Invalid user email")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Please enter your gender")
    private String gender;

    @NotBlank(message = "about cannot be blank")
    private String about;

    //custom validation
    @JsonIgnore
    private String imageName;
}
