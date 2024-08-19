package com.abn.nl.service;

import com.abn.nl.dtos.OrderDto;
import com.abn.nl.dtos.ProductDTO;

import java.util.List;


public interface ProductService {

    List<ProductDTO> getProducts(List<String> productNames, String traceId);
    Long placeOrder(OrderDto orderDto, String traceId);
}
