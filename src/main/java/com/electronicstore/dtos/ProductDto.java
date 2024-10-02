package com.electronicstore.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ProductDto {


    private String productId;
    @NotBlank(message = "title is required")
    private String title;
    private String description;
    private int price;
    private int discountedPrice;
    private String sellerName;
    private int quantity;
    private Date addedDate;
    private boolean live;
    private boolean stock;
    private String productImageName;
}
