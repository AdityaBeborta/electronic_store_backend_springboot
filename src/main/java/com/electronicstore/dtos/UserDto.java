package com.electronicstore.dtos;
import com.electronicstore.entities.Cart;
import com.electronicstore.entities.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Schema(accessMode = Schema.AccessMode.READ_ONLY,description = "USER DTO SCHEMA")
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

    @Schema(hidden = true)
    private CartDto cart;

    private List<Roles> roles;
}
