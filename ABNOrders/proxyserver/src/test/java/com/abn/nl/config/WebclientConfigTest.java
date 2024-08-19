package com.abn.nl.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WebclientConfigTest {
    @InjectMocks
    private WebclientConfig webclientConfig;

    @Test
    void exchangeWebClient() {
      WebClient result=  webclientConfig.exchangeWebClient();
      assertNotNull(result);
    }

}