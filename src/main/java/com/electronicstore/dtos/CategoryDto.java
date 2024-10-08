package com.electronicstore.dtos;

import com.electronicstore.entities.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryDto {


    private String categoryId;
    @Size(min = 3,max = 60,message = "category length should be minimum 3 and maximum 60")
    @NotBlank(message = "category title is required")
    private String categoryTitle;
    @NotBlank(message = "category description is required")
    private String categoryDescription;
    private String categoryCoverImage;
    @JsonIgnore
    private List<ProductDto> products;
}
