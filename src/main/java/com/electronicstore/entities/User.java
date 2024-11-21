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
    @Column(name = "user_email", unique = true)
    private String email;
    @Column(name = "user_password", length = 90)
    private String password;
    private String gender;
    @Column(length = 1000)
    private String about;
    @Column(name = "user_image_name")
    private String imageName;

    //create a mapping of user with cart inn such a way that one user can have only one cart
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Cart cart;

    //mapping for user and order
    //one user can place multiple orders
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
            name ="user_role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private List<Roles> roles = new ArrayList<>();
}
