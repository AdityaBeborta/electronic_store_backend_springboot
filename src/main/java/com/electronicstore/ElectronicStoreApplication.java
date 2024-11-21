package com.electronicstore;

import com.electronicstore.entities.Roles;
import com.electronicstore.entities.User;
import com.electronicstore.helper.ApplicationConstants;
import com.electronicstore.repositories.RoleRepository;
import com.electronicstore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ElectronicStoreApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        Roles roleAdmin = this.roleRepository.findByRoleType(ApplicationConstants.ROLE_ADMIN).orElse(null);
        Roles roleGuest = this.roleRepository.findByRoleType(ApplicationConstants.ROLE_GUEST).orElse(null);
        if (roleAdmin == null) {
            roleAdmin = new Roles();
            roleAdmin.setRoleType(ApplicationConstants.ROLE_ADMIN);
            roleAdmin.setRoleId(UUID.randomUUID().toString());
            this.roleRepository.save(roleAdmin);
        }
        if (roleGuest == null) {
            roleGuest = new Roles();
            roleGuest.setRoleType(ApplicationConstants.ROLE_GUEST);
            roleGuest.setRoleId(UUID.randomUUID().toString());
            this.roleRepository.save(roleGuest);
        }
        User user = this.userRepository.findByEmail("sam@gmail.com").orElse(null);
        if (user == null) {
            user = new User();
            user.setUserId(UUID.randomUUID().toString());
            user.setEmail("sam@gmail.com");
            user.setPassword(passwordEncoder.encode("sam@123"));
            user.setRoles(List.of(roleAdmin, roleGuest));
			this.userRepository.save(user);
        }

    }
}
