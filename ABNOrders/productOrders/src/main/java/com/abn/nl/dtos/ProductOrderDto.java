package com.abn.nl.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductOrderDto {
    private String productName;
    @Size(min = 1, message = "Please provide the quantity of the product")
    private int quantity;
    @NotBlank(message = "Please provide the seller from whom you want to buy")
    private String seller;
    @NotBlank(message = "Please provide the brand of the product")
    private String brand;

}
