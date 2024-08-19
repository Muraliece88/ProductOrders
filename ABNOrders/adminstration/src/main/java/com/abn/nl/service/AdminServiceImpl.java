package com.abn.nl.service;


import com.abn.nl.Order;
import com.abn.nl.ProductsRepo;
import com.abn.nl.dto.OrderDto;
import com.abn.nl.dto.ProductStatsDto;
import com.abn.nl.exceptions.OrderNotFoundException;
import com.abn.nl.mappers.OrderMapper;

import com.abn.nl.OrderRepo;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminServices{
    private  final OrderRepo orderRepo;
    private  final ProductsRepo productRepo;
    private final OrderMapper mappers = Mappers.getMapper(OrderMapper.class);


    public AdminServiceImpl(OrderRepo orderRepo, ProductsRepo productRepo) {

        this.orderRepo = orderRepo;

        this.productRepo = productRepo;
    }

    @Override
    public OrderDto searchOrderById(String id) {
      Order order=  orderRepo.findById(Long.parseLong(id)).orElseThrow(() -> new OrderNotFoundException(
                "No order found for the id:"+id));


       return mappers.INSTANCE.
                getOrderToDto(order);
    }

    @Override
    public List<ProductStatsDto> getTopSellingProducts() {

        return productRepo.getTopSellingProducts(LocalDate.now())
            .stream().map(
                    product->
                    {
                        if( ((Number) product[1]).doubleValue()<10) {
                           return new ProductStatsDto(
                                    ((String) product[0]).toString(),
                                    ((Number) product[1]).intValue(),
                                    ((Number) product[2]).doubleValue(),
                                    ((Number) product[3]).intValue(),
                                    (true));

                        }
                        else {
                            return  new ProductStatsDto(
                                    ((String) product[0]).toString(),
                                    ((Number) product[1]).intValue(),
                                    ((Number) product[2]).doubleValue(),
                                    ((Number) product[3]).intValue(),
                                    (false));
                        }
                    }).toList();



    }

    @Override
    public List<ProductStatsDto> getLeastSellingProducts() {
        YearMonth currentMonth=YearMonth.now();
       return productRepo.
                getLeastSellingProducts(currentMonth.atDay(1),currentMonth.atEndOfMonth())
                .stream().map( product->
                        new ProductStatsDto(
                                ((String) product[0]).toString(),
                                ((Number) product[1]).intValue(),
                                ((Number) product[2]).doubleValue(),
                                ((Number) product[3]).intValue(),
                                (null))).toList();

    }

}
