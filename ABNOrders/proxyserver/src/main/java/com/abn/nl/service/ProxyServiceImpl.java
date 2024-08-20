package com.abn.nl.service;

import com.abn.nl.dto.ExchangeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class ProxyServiceImpl implements ProxyService {

    private final WebClient exchangeClient;
    private final WebClient messagingClient;
    private final String prefix="/Messages.json";
    private final String USD="USD";

    public ProxyServiceImpl(@Qualifier("exchangeClient")WebClient exchangeClient,@Qualifier("messagingClient") WebClient messagingClient) {
        this.exchangeClient = exchangeClient;
        this.messagingClient = messagingClient;
    }

    /**
     * Method to send message to the requster
     * @param userName
     * @param password
     * @param message
     * @param traceId
     * @return
     */
    @Override
    public Mono<String> sendMessage(String userName, String password, MultiValueMap<String, String> message,String traceId) {

        log.info("Processing message for trace: {}"+ traceId);
            return    messagingClient.post().uri(userName+prefix).
                headers(httpHeaders -> httpHeaders.setBasicAuth(userName,password))
                .accept(MediaType.APPLICATION_FORM_URLENCODED).
                body(BodyInserters.fromFormData(message))
                .exchangeToMono(clientResponse -> Mono.just(String.valueOf(clientResponse.statusCode().value()))).
                    onErrorResume(error-> Mono.just(error.getMessage()));
    }

    /**
     * Method to fetch the dollar equivalent value
     * @param currencies
     * @param traceId
     * @return
     */

    @Override
    public Map<String, Double> fetchRates(Set<String> currencies,String traceId) {
        Map<String, Double> usdRates = new HashMap<>();
        log.info("Processing request for trace: {}"+ traceId);
        Flux<ExchangeResponse> responseMono=Flux.fromIterable(currencies).flatMap(
                        currency-> exchangeClient.get().uri(currency)
                                .retrieve().bodyToFlux(ExchangeResponse.class))
                .onErrorResume(e->Mono.error(e));
        responseMono.collectList().blockOptional().ifPresent(exchangeResponses -> {
            exchangeResponses.forEach(exchangeResponse -> {
                usdRates.put(exchangeResponse.getBase(),exchangeResponse.getRates().get(USD));
            });
        });
        return usdRates;
    }

}
