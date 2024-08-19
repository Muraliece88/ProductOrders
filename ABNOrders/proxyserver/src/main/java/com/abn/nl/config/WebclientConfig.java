package com.abn.nl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebclientConfig {
    @Value("${exchange.api.name}")
    private String exchangeUri;
    @Value("${message.api.name}")
    private String messageUri;

    @Bean("exchangeClient")
    public WebClient exchangeWebClient() {
        return WebClient.builder().
                baseUrl(exchangeUri)
                .build();
    }
    @Bean("messagingClient")
    public WebClient messagingWebClient() {
        return WebClient.builder().
                baseUrl(messageUri)
                .build();
    }

}
