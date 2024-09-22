package com.electronicstore.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {
    @Id
    private String userId;
    @Column(name = "user_name")
    private String name;
    @Column(name = "user_email",unique = true)
    private String email;
    @Column(name = "user_password",length = 10)
    private String password;
    private String gender;
    @Column(length = 1000)
    private String about;
    @Column(name = "user_image_name")
    private String imageName;
}
