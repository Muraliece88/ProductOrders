package com.abn.nl.service;



import com.abn.nl.dto.OrderDto;
import com.abn.nl.dto.ProductStatsDto;

import java.util.List;

public interface AdminServices {
   OrderDto searchOrderById(String id);
   List<ProductStatsDto> getTopSellingProducts();
   List<ProductStatsDto> getLeastSellingProducts();

}
