package com.electronicstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Category {

    @Id
    private String categoryId;

    private String categoryTitle;

    private String categoryDescription;

    private String categoryCoverImage;
}
