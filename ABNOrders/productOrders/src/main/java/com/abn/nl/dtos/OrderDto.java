package com.abn.nl.dtos;

import com.abn.nl.Product;
import com.abn.nl.validator.MobileNumberConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class OrderDto {
    @NotBlank(message = "Please provide your Name")
    private String customerName;
    @NotBlank(message = "Please provide your mobile number")
    @MobileNumberConstraint
    private String mobileNumber;
    private Set<ProductOrderDto> products;


}
