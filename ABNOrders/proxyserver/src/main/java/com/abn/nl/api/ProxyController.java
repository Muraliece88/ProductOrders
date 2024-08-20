package com.abn.nl.api;

import com.abn.nl.service.ProxyService;
import com.abn.nl.service.ProxyServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/v1/proxies")
public class ProxyController {
    private final ProxyService proxyService;
    private final String exchange = "/exchange";
    private final String messaging = "/messaging";
    private static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";


    public ProxyController(ProxyServiceImpl proxyService) {
        this.proxyService = proxyService;
    }


    /**
     * Method triggers when request to place a message
     * @param userName
     * @param password
     * @param message
     * @return
     */
    @PostMapping (path = messaging,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    private Mono<ResponseEntity<String>> sendMessage(@RequestHeader(name = "userName") String userName,
                                          @RequestHeader(name = "password") String password,
                                          @RequestBody MultiValueMap<String, String> message) {
        String traceId=Optional.ofNullable(MDC.get(CORRELATION_ID_HEADER_NAME)).get();
        log.info("Request has been received to send message for trace: {}"+ traceId);
        Mono<String> result = proxyService.sendMessage(userName, password, message,traceId);

        log.info("Returning response for trace: {}"+ traceId);
        return  result.flatMap(response-> Mono.just(ResponseEntity.ok(response)))
                .onErrorResume(throwable -> Mono.just(ResponseEntity.status(500).body(throwable.getMessage())));
    }

    /**
     * method to fetch the exchange rates
     * @param currencies
     * @return
     */
    @GetMapping(path = exchange
            , produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<Map<String, Double>> getExchangeRates(@RequestParam(name = "currency") Set<String> currencies) {
        String traceId=Optional.ofNullable(MDC.get(CORRELATION_ID_HEADER_NAME)).get();
        log.info("Request has been received to send message for trace: {}"+ traceId);
        return ResponseEntity.ok(proxyService.fetchRates(currencies,traceId));

    }
}

