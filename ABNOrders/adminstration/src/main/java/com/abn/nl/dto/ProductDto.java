package com.abn.nl.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"name", "brand","seller"})
public class ProductDto {
    private String name;
    private String brand;
    private String seller;
}
