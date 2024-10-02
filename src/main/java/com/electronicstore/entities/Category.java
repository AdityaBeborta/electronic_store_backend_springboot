package com.electronicstore.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Category {

    @Id
    private String categoryId;

    private String categoryTitle;

    private String categoryDescription;

    private String categoryCoverImage;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "category")
    private List<Product> products;
}
