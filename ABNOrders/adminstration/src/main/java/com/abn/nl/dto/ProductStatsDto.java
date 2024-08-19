package com.abn.nl.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductStatsDto {
    private String productName;
    private int stockleft;
    private double price;
    private int productCount;
    private Boolean lowInStock;
}