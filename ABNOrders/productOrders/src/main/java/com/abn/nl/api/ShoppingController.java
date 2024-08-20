package com.abn.nl.api;

import com.abn.nl.dtos.OrderDto;
import com.abn.nl.dtos.ProductDTO;
import com.abn.nl.service.ProductServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
public class ShoppingController {
    private final String fetchPath="/fetchProducts";
    private final String orderPath="/placeOrder";
    private final ProductServiceImpl serviceImpl;


    public ShoppingController(ProductServiceImpl serviceImpl) {
        this.serviceImpl = serviceImpl;

    }

    /**
     * Method to fetch the product details from DB
     * @param products list of params as a search condition
     * @return empty if no matching condition or list of product details
     */

    @GetMapping(path = fetchPath
            ,produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> getProducts(@RequestParam(name="products",required = false) List<String> products)
    {

        String correlationId = UUID.randomUUID().toString();
        log.debug("Request to fetch products with trace ID: {}", correlationId);
        List<ProductDTO> productList=serviceImpl.getProducts(products,correlationId);
        if(productList.isEmpty())
        {
            log.info("No matching products available for the trace ID: {}", correlationId);
            return ResponseEntity.noContent().build();
        }
        else {
            log.info("Returning the product details for the trace ID: {}", correlationId);
            return ResponseEntity.ok(productList);
        }
    }

    /**
     * Method to create new orders
     * @param  order dto
     * @return a message wif order id placed else not fo
     */
 @PostMapping(path = orderPath,
                consumes =  MediaType.APPLICATION_JSON_VALUE
            ,produces =MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<String> placeOrder( @Valid @RequestBody OrderDto order)
    {
        String correlationId = UUID.randomUUID().toString();
        log.debug("Request place order with trace ID: {}", correlationId);
        Long id=serviceImpl.placeOrder(order,correlationId);
        if(id!=null) {
            return ResponseEntity.status(201)
                    .contentType(MediaType.APPLICATION_JSON_UTF8).
                    body("Your order has been placed successfully and the id is: "+id);
        }
        else {
            log.info("Order wasnt placed with trace ID: {}", correlationId);
            return ResponseEntity.status(204)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body("Order has not been placed");
        }
    }

}
