package com.abn.nl.service;

import com.abn.nl.Order;
import com.abn.nl.OrderRepo;
import com.abn.nl.Product;
import com.abn.nl.ProductsRepo;
import com.abn.nl.dtos.OrderDto;
import com.abn.nl.dtos.ProductDTO;
import com.abn.nl.dtos.ProductOrderDto;
import com.abn.nl.exceptions.NotificationException;
import com.abn.nl.exceptions.ProductNotFoundException;
import com.abn.nl.mappers.ProductMappers;
import com.abn.nl.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
public class ProductServiceImpl implements ProductService{
    private final ProductsRepo productsRepo;
    private final OrderRepo orderRepo;
    private final Utils utils;
    @Value("${proxy.base.path}")
    private String proxyBasePath;
    @Value("${proxy.api.app.name}")
    private String proxyAppName;
    @Value("${proxy.api.exchange}")
    private String exchangeapi;
    @Value("${proxy.api.message}")
    private String messageapi;
    private final ProductMappers mappers = Mappers.getMapper(ProductMappers.class);


    public ProductServiceImpl(ProductsRepo productsRepo, OrderRepo orderRepo, Utils utils) {
        this.productsRepo = productsRepo;
        this.orderRepo = orderRepo;
        this.utils = utils;

    }

    /**
     * This method is to get the product detials based on search condition if no param passed then all product is returned
     * @param productNames search condition
     * @param traceId for logging and tracing
     * @return
     */

    @Override
    public List<ProductDTO> getProducts(List<String> productNames, String traceId) {

        if(productNames==null)
        {
            log.info("fetching all products for the traceId: {}", traceId);
            return mappers.INSTANCE.getProductsToDto(productsRepo.findAll());
        }
        else {
            log.info("fetching products: {} for the traceId: {}",productNames, traceId);
            productsRepo.findProductByNameIn(productNames);
            return mappers.INSTANCE.getProductsToDto(productsRepo.findProductByNameIn(productNames));
        }
    }


    /**Method to place a new order
     *
     * @param orderDto Order input details
     * @param traceId logging and tracing
     * @return Id if the order is placed
     */
    @Override
    @Transactional
    public Long placeOrder(OrderDto orderDto,String traceId) {
        Set<Product> products=new HashSet<>();
        orderDto.getProducts().forEach(product -> {
          Product prod=  productsRepo.findProductByNameAndBrandAndSeller
                    (product.getProductName(),product.getBrand(),product.getSeller()).
                  orElseThrow(() -> new ProductNotFoundException(
                          String.format("Product: %s not found for the brand: %s and seller: %s",
                          product.getProductName(),product.getBrand(),product.getSeller())));
          products.add(prod);
        });
        log.debug("All requested products found {}"+traceId);
        Order order=new Order();
        Set<Product> productSet=new HashSet<>();
        products.forEach(product -> productSet.add(product));
        order.setContactNumber(orderDto.getMobileNumber());
        order.setOrderedBy(orderDto.getCustomerName());
        order.setQuantity(orderDto.getProducts().stream().
                mapToInt(ProductOrderDto::getQuantity).sum());
        order.setOrderedOn(LocalDate.now());
        order.setProducts(productSet);
        Order placedOrder=orderRepo.save( order);
        log.info("Order Id: {} has been placed with trace{}"+ placedOrder.getId(),traceId);
        orderDto.getProducts().forEach(product -> {
            products.forEach(updateProduct -> {
                if(updateProduct.getName().equalsIgnoreCase(product.getProductName())) {
                    updateProduct.setStockleft(
                            (updateProduct.getStockleft())-(product.getQuantity()));
                    productsRepo.save(updateProduct);
                    log.info("Updated product: {} with trace{}",product.getProductName(),traceId);
                }
            });

        });
        try {
            log.debug("SMS Notification  is been triggered for: {}", traceId);
            utils.sendNotification(placedOrder.getId(),
                        orderDto,products,
                        proxyBasePath,proxyAppName,exchangeapi,messageapi,traceId);
        }
            catch (NotificationException e) {
            log.error("Unable to send notify requester for the trace: {}", traceId);
            log.error(e.getMessage());
        }
        return placedOrder.getId();
    }



}
