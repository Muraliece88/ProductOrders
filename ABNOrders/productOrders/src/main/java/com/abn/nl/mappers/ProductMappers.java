package com.abn.nl.mappers;

import com.abn.nl.Order;
import com.abn.nl.Product;
import com.abn.nl.dtos.OrderDto;
import com.abn.nl.dtos.ProductDTO;
import com.abn.nl.dtos.ProductOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMappers {
    ProductMappers INSTANCE = Mappers.getMapper(ProductMappers.class);
    @Mapping(source = "name",target = "productName")
    ProductDTO getProductsToDto (Product products);

    List<ProductDTO> getProductsToDto (List<Product> products);
    Product orderToProduct(ProductOrderDto productOrderDto);


}
