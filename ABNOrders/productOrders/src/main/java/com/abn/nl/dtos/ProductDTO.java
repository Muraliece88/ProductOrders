package com.abn.nl.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private String productName;
    private String brand;
    private String stockleft;
    private BigDecimal price;
    private String currency;
    private String seller;

}
