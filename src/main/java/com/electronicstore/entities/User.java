package com.electronicstore.entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    //create a mapping of user with cart inn such a way that one user can have only one cart
    @OneToOne(mappedBy = "user",fetch = FetchType.LAZY)
    private Cart cart;

    //mapping for user and order
    //one user can place multiple orders
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "user")
    private List<Order> orders = new ArrayList<>();
}
