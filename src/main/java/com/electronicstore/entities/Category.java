package com.electronicstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Category {

    @Id
    private String categoryId;
    @Column(unique = true)
    private String categoryTitle;

    private String categoryDescription;

    private String categoryCoverImage;
}
