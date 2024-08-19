package com.abn.nl.service;


import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

public interface ProxyService {

    Mono<String> sendMessage(String userName, String password, MultiValueMap<String, String> message,String traceId);
    Map<String, Double> fetchRates(Set<String> currency,String traceId);

}
