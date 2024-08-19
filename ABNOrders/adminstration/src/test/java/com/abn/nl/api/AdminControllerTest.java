package com.abn.nl.api;

import com.abn.nl.Order;
import com.abn.nl.OrderRepo;
import com.abn.nl.ProductsRepo;
import com.abn.nl.dto.ProductStatsDto;
import com.abn.nl.dto.ReportingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    private final String searchOrder = "/api/v1/admin/search/order/{orderId}";
    @MockBean
    private OrderRepo orderRepo;
    @MockBean
    private ProductsRepo productsRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    void getOrder() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setOrderedBy("test");
        when(orderRepo.findById(anyLong())).thenReturn(Optional.of(order));
        mockMvc.perform(get
                ("/api/v1/admin/search/order/{orderId}","123"))

                .andExpect(status().is(200));
    }

}