package com.electronicstore.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Roles {

    @Id
    private String roleId;
    private String roleType;
}
