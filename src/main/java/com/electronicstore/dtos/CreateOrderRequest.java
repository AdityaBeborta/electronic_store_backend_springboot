package com.electronicstore.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateOrderRequest {

    @NotBlank(message = "user id is required !!")
    private String userId;
    private String orderStatus = "PENDING";
    private String paymentStatus = "NOTPAID";
    private String paymentMode;
    @NotBlank(message = "Address is required !!")
    private String billingAddress;
    @NotBlank(message = "Phone number is required !!")
    private String billingPhone;
    @NotBlank(message = "Billing name  is required !!")
    private String billingName;


}