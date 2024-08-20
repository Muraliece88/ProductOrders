package com.abn.nl.api;

import com.abn.nl.Product;
import com.abn.nl.ProductsRepo;
import com.abn.nl.dtos.ProductDTO;
import com.abn.nl.mappers.ProductMappers;
import com.abn.nl.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;

import javax.sql.DataSource;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ShoppingControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ProductServiceImpl serviceImpl;
    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement statement;
    @Mock
    private ProductMappers mappers;
    @Autowired
    private ProductsRepo repo;



    private final String getURL = "/api/v1/products/fetchProducts";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @Test
    void getAllProducts() throws Exception {
        Product product = new Product();
        product.setName("Shirt");
        product.setCurrency("USD");
        product.setStockleft(5);
        product.setSeller("seller");
        List<Product> mockproduct=new ArrayList<>();
        mockproduct.add(product);
        when(dataSource.getConnection()).thenReturn(connection);
        mockMvc.perform(get(getURL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(jsonPath("$[0].productName").exists());

    }
    @Test

    void getProductswithparam() throws Exception {
        Product product = new Product();
        product.setName("Shirt");
        product.setCurrency("USD");
        product.setStockleft(5);
        product.setSeller("seller");
        List<Product> mockproduct=new ArrayList<>();
        mockproduct.add(product);
        when(dataSource.getConnection()).thenReturn(connection);
        mockMvc.perform(get(getURL)
                        .param("productNames", "Shirt")
                        .param("productNames", "Jean")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andExpect(jsonPath("$[0].productName").exists());

    }
}