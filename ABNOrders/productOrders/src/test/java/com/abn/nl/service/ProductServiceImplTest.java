package com.abn.nl.service;

import com.abn.nl.Order;
import com.abn.nl.OrderRepo;
import com.abn.nl.Product;
import com.abn.nl.ProductsRepo;
import com.abn.nl.dtos.OrderDto;
import com.abn.nl.dtos.ProductDTO;
import com.abn.nl.dtos.ProductOrderDto;
import com.abn.nl.exceptions.ProductNotFoundException;
import com.abn.nl.mappers.ProductMappers;
import com.abn.nl.utils.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
@InjectMocks
private ProductServiceImpl service;
@Mock
private ProductsRepo productsRepo;
    @Mock
    private OrderRepo orderRepo;
    @Mock
    private Utils mockUtils;


    @Test
    void getProducts() {
        List<Product> mockList=new ArrayList<>();
        Product mockProduct=new Product();
        mockProduct.setName("product");
        mockProduct.setSeller("seller");
        mockProduct.setBrand("brand");
        mockList.add(mockProduct);
        List<ProductDTO> mockDto=new ArrayList<>();
        ProductDTO dto=new ProductDTO();
        dto.setProductName("product");
        dto.setSeller("seller");
        dto.setBrand("brand");
        mockDto.add(dto);
        when(productsRepo.findProductByNameIn(anyList())).thenReturn(mockList);
        assertEquals(1,service.getProducts(List.of("Shirt"),"123").size());
    }

    @Test
    void placeOrderException() {
        OrderDto orderDto=new OrderDto();
        Set<ProductOrderDto> products=new HashSet<>();
        ProductOrderDto orderDto1=new ProductOrderDto();
        orderDto1.setProductName("product");
        orderDto1.setSeller("seller");
        orderDto1.setBrand("brand");
        ProductOrderDto orderDto2=new ProductOrderDto();
        orderDto2.setProductName("product2");
        orderDto2.setSeller("seller2");
        orderDto2.setBrand("brand2");
        orderDto.setCustomerName("customer");
        orderDto.setMobileNumber("12324567890123");
        products.add(orderDto1);
        products.add(orderDto2);
        orderDto.setProducts(products);
        assertThrows(
                ProductNotFoundException.class,
                () ->service.placeOrder(orderDto,"123"),
                "Product: product2 not found for the brand: brand2 and seller: seller2"
        );
    }
    @Test
    void placeOrder()
    {
        Product product=new Product();
        Order placedOrder=mock(Order.class);
        DiscoveryClient mockDiscovery=mock(DiscoveryClient.class);
        ServiceInstance mockserv=mock(ServiceInstance.class);
        product.setName("product");
        product.setSeller("seller");
        product.setBrand("brand");
        Product mockProduct=mock(Product.class);
        OrderDto orderDto=new OrderDto();
        Set<ProductOrderDto> products=new HashSet<>();
        ProductOrderDto orderDto1=new ProductOrderDto();
        orderDto1.setProductName("product");
        orderDto1.setSeller("seller");
        orderDto1.setBrand("brand");
        ProductOrderDto orderDto2=new ProductOrderDto();
        orderDto2.setProductName("product2");
        orderDto2.setSeller("seller2");
        orderDto2.setBrand("brand2");
        orderDto.setCustomerName("customer");
        orderDto.setMobileNumber("12324567890123");
        products.add(orderDto1);
        products.add(orderDto2);
        orderDto.setProducts(products);
        when( productsRepo.
                findProductByNameAndBrandAndSeller
                        (anyString(),anyString(),anyString())).thenReturn(Optional.of(product));
        when(orderRepo.save(any())).thenReturn(placedOrder);
        when(placedOrder.getId()).thenReturn(1L);
        doNothing().when(mockUtils).sendNotification
                (any(),any(),any()
                        ,any(),any(),
                        any(),any(),anyString());
        assertNotNull(service.placeOrder(orderDto,"123"));
    }

}