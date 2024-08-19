package com.abn.nl.service;

import com.abn.nl.Order;
import com.abn.nl.OrderRepo;
import com.abn.nl.ProductsRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {
    @InjectMocks
    private AdminServiceImpl adminService;
    @Mock
    private OrderRepo orderRepo;
    @Mock
    private ProductsRepo productsRepo;

    @Test
    void searchOrderById() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderedBy("test");
        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        assertNotNull( adminService.searchOrderById("1"));
    }

    @Test
    void getTopSellingProducts() {
        assertNotNull(productsRepo.getTopSellingProducts(LocalDate.now()));
    }

    @Test
    void getLeastSellingProducts() {
        assertNotNull(productsRepo.getLeastSellingProducts
                (LocalDate.of(2024,8,1),LocalDate.of(2024,8,30)));
    }
}