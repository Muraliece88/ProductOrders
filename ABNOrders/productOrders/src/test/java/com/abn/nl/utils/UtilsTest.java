package com.abn.nl.utils;

import com.abn.nl.Product;
import com.abn.nl.dtos.OrderDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UtilsTest {
    @InjectMocks
    private Utils utils;
    @Mock
    private DiscoveryClient discoveryClient;
    @Mock
    private ServiceInstance serviceInstance;
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec headersUriSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;


    @Test
    void sendNotification() throws URISyntaxException {
        URI uri = new URI("http://localhost:8086");
        OrderDto dto = new OrderDto();
        Product product = new Product();
        product.setName("product");
        product.setSeller("seller");
        product.setBrand("brand");
        dto.setMobileNumber("1234567890098");
        dto.setCustomerName("test");
        when(discoveryClient.getInstances(any())).thenReturn(List.of(serviceInstance));
        when(serviceInstance.getUri()).thenReturn(uri);
        assertThrows(
                Exception.class,
                () -> utils.sendNotification(1l,dto,
                        Set.of(product),"/tset",
                        "/test","/test","/test","123"),
                "#block terminated with an error"
        );
    }
}