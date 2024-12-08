package com.electronicstore.repositories;

import com.electronicstore.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles,String> {
    Optional<Roles> findByRoleType(String roleType);
}
