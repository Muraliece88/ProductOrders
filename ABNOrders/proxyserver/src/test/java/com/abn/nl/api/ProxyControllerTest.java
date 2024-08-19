package com.abn.nl.api;

import com.abn.nl.service.ProxyServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProxyControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ProxyServiceImpl serviceImpl;
    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement statement;
    private  MockedStatic mockMdc;

    private final String postURL = "/api/v1/proxies/messaging";
    private final String getURL = "/api/v1/proxies/exchange";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        mockMdc=mockStatic(MDC.class);
    }

    @Test

    void sendMessage() throws Exception {
        MultiValueMap<String, String> message= new LinkedMultiValueMap<>();
        mockMdc.when(()->MDC.get(anyString())).thenReturn("mdc");
        message.add("To", "+1456768900898");
        message.add("From", "+917812456768");
        message.add("Body", "helo");
        HttpHeaders headers = new HttpHeaders();
        headers.set("userName","name");
        headers.set("userName","name");

        mockMvc.perform(post(postURL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .params(message)
                        .header("userName","name")
                        .header("password","password"))
                .andExpect(status().is(200));
    }
    @Test
    @WithMockUser(username = "dummy", password = "dummy", roles = "ADMIN")
    void exchangeRate() throws Exception {

        mockMdc.when(()->MDC.get(anyString())).thenReturn("mdc");

        MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
        Set<String> currency=new HashSet<>();
        currency.add("INR");
        currency.forEach(curr->params.add("currency",curr));
        mockMvc.perform(get(getURL)
                        .params(params)
                        .header("userName","name")
                        .header("password","password"))
                .andExpect(status().is(200));
    }

    @AfterEach
     void tearDown() {
        mockMdc.close();
    }

}