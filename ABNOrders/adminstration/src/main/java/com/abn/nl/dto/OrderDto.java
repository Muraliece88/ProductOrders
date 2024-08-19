package com.abn.nl.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@JsonPropertyOrder({ "id", "orderedBy","orderDate","contactNumber", "quantity", "productDetails" })
public class OrderDto {
    private Long id;
    private Set<ProductDto> productDetails;
    private LocalDate orderDate;
    private String orderedBy;
    private String contactNumber;
    private int quantity;
}
