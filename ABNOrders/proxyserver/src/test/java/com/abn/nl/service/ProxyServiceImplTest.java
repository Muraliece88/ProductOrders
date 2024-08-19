package com.abn.nl.service;

import org.apache.commons.lang.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProxyServiceImplTest {
    @InjectMocks
    private ProxyServiceImpl proxyService;
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestBodyUriSpec bodyUriSpec;
    @Mock
    private WebClient.RequestBodySpec bodySpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;
    @Mock
    private WebClient.RequestHeadersSpec headersSpec;
    @Mock
    private WebClient.RequestHeadersUriSpec headersUriSpec;


    @Test
    void sendMessage()  {
        MultiValueMap<String, String> message= new LinkedMultiValueMap<>();
        Mono mockMono=mock(Mono.class);
        message.add("To", "+1456768900898");
        message.add("From", "+917812456768");
        message.add("Body", "helo");
        when(webClient.post()).thenReturn(bodyUriSpec);
        when(bodyUriSpec.uri(Mockito.anyString())).thenReturn(bodySpec);
        when(bodySpec.headers(any())).thenReturn(bodySpec);
        when(bodySpec.accept(any())).thenReturn(bodySpec);
        when(bodySpec.body(any())).thenReturn(headersSpec);
        when(headersSpec.exchangeToMono(any())).thenReturn(mockMono);
        assertNull(proxyService.sendMessage("user","pass",message,"123"));
    }

}