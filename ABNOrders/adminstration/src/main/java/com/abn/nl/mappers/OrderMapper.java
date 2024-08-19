package com.abn.nl.mappers;


import com.abn.nl.Order;
import com.abn.nl.dto.OrderDto;
import com.abn.nl.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    @Mapping(source = "orderedOn",target = "orderDate")
    @Mapping(source = "products",target = "productDetails")
    OrderDto getOrderToDto (Order order);

}
