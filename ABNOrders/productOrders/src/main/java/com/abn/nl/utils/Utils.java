package com.abn.nl.utils;

import com.abn.nl.Product;
import com.abn.nl.dtos.OrderDto;
import com.abn.nl.exceptions.NotificationException;
import com.abn.nl.exceptions.ProxyNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Utils {
    @Value("${messaging.sid}")
    private  String accountSid;
    @Value("${msessaging.pwd}")
    private  String accountPwd;
    @Value("${proxy.api.user.name}")
    private String proxyUser;
    @Value("${proxy.api.user.password}")
    private String proxyPass;
    private final DiscoveryClient discoveryClient;

    public Utils(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }


    /**
     * Non sync method to send the nottification  once order is placed
     * @param id
     * @param orderDto
     * @param products
     * @param proxyBasePath
     * @param proxyAppName
     * @param exchangeapi
     * @param messageapi
     * @param traceId
     */
    @Async
    @CircuitBreaker(name = "messsagingService", fallbackMethod = "noificationFailure")
    public void sendNotification(Long id,
                                 OrderDto orderDto,
                                 Set<Product> products,
                                 String proxyBasePath,String proxyAppName,
                                 String exchangeapi,String messageapi,
                                 String traceId
                                 )
    {
        String hostPort=fetchProxyDetails(discoveryClient,proxyAppName);
        WebClient client=getWebClient(hostPort+proxyBasePath);
        BigDecimal total=calculateTotalAmt(products,client,exchangeapi,orderDto,traceId);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("To",orderDto.getMobileNumber());
        formData.add("Body",String.format("Your order number is %s please pay: $%s",id,total.setScale(2)));
        formData.add("From","+19382536521");
         client.post().uri(uriBuilder ->
                        uriBuilder.path(messageapi).build()).
                    headers(httpHeaders ->{
                        httpHeaders.set("userName", accountSid);
                        httpHeaders.set("password", accountPwd);
                        httpHeaders.set("X-Correlation-ID", traceId);
                        })
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)).
                bodyValue(formData).exchangeToMono(
                        clientResponse ->
                                Mono.just(String.valueOf(clientResponse.statusCode().value())))
                 .blockOptional().ifPresent(response->{
                     if(response.equals("200"))
                     {
                         log.info("Message delievred to the requester: {}" , traceId );
                     }
                     else {
                         log.info("Message nor delievred to the requester: {}" , traceId );
                     }
                 });

        }

    /**
     * Calcuate amout the rrequester has to pay
     *
     * @param products
     * @param webclient
     * @param exchangeApi
     * @param orderDto
     * @param traceId
     * @return
     */
    private BigDecimal calculateTotalAmt(Set<Product> products,
                                         WebClient webclient,
                                         String exchangeApi,OrderDto orderDto,String traceId)
    {
        log.info("calculate amt request being processed for the trce: {}" +traceId);
        Map<String, Double> dollarVal= new HashMap<>();
        Set<String> currencies=new HashSet<>();
        products.forEach(product -> currencies.add(product.getCurrency()));

            Mono<Object> exchangeMono= webclient.get().uri(uriBuilder ->
                       uriBuilder.path(exchangeApi).queryParam("currency",currencies).
                               build())
                    .headers(httpHeaders-> httpHeaders.set("X-Correlation-ID", traceId))
                    .retrieve().
                                bodyToMono(new ParameterizedTypeReference<>() {
            }).onErrorResume(e->Mono.error(e));

            exchangeMono.blockOptional().ifPresent(exchangeResponses -> {
            Map<String,Double> result= (Map<String, Double>) exchangeResponses;
            dollarVal.putAll(result);
                });


        Map<String, BigDecimal> prodDollar= products.stream().
                collect(Collectors.toMap
                        (Product::getName,product ->
                            product.getPrice()
                                    .multiply(BigDecimal.valueOf(
                                            dollarVal.get(product.getCurrency())))));
        return orderDto.getProducts().stream()
                .map(product -> prodDollar.get(product.getProductName())
                                .multiply(BigDecimal
                                        .valueOf(product.getQuantity()))).reduce(BigDecimal.ZERO,BigDecimal::add);



    }

    /**
     * External apis exposed over proxy and method to get them
     * @param discoveryClient
     * @param proxyAppName
     * @return
     */

    private String  fetchProxyDetails(  DiscoveryClient discoveryClient,String proxyAppName)
    {
        return discoveryClient.getInstances(proxyAppName).
                stream().map(serviceInstance -> serviceInstance.getUri().toString())
                .findAny().orElseThrow(()-> new ProxyNotFoundException("No such proxy server available"));
    }

    /**
     * Construct the client for calling the external APIs
     * @param url
     * @return
     */

    private WebClient getWebClient(String url) {
        String encoded=Base64.getEncoder().
                encodeToString((proxyUser+":"+proxyPass).getBytes(StandardCharsets.UTF_8));
        return WebClient.builder().
                baseUrl(url)
                .defaultHeaders(header->header.setBasicAuth(encoded))
                .build();
    }


    /**
     * Triggers in case of the API unavilability
     * @param id
     * @param orderDto
     * @param products
     * @param proxyBasePath
     * @param proxyAppName
     * @param exchangeapi
     * @param messageapi
     * @param traceId
     * @param throwable
     */
    private void noificationFailure(Long id,
                                    OrderDto orderDto,
                                    Set<Product> products,
                                    String proxyBasePath,String proxyAppName,
                                    String exchangeapi,String messageapi,String traceId, Throwable throwable)
    {
        log.error(throwable.getMessage());
        throw new NotificationException(
                String.format("Unable to notify %s for the orderId %s due to %s",
                        orderDto.getCustomerName(), id,throwable.getMessage()));

    }
}
