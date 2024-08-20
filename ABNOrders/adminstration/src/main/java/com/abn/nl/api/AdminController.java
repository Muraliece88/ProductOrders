package com.abn.nl.api;

import com.abn.nl.dto.OrderDto;
import com.abn.nl.dto.ReportingType;
import com.abn.nl.service.AdminServiceImpl;
import com.abn.nl.validator.RepTypeConstraint;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final String searchOrders="/search/order/{orderId}";
    private final String reportPath="/reporting";
    private final AdminServiceImpl adminService;

    public AdminController(AdminServiceImpl adminService) {
        this.adminService = adminService;
    }

    /**
     * method to searach order based on id
     * @param orderId serach condition
     * @return order is fetched if exists otherwise no context is retured
     */
    @GetMapping(path = searchOrders
            ,produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> getProducts(@PathVariable(name="orderId") String orderId)
    {
        String correlationId = UUID.randomUUID().toString();
        log.debug("Request search  order with trace ID: {}", correlationId);
       OrderDto orderDetails= adminService.searchOrderById(orderId);
        if(Optional.ofNullable(orderDetails).isPresent()){
            return ResponseEntity.ok(orderDetails);
        }
        else{
            return ResponseEntity.status(204).
                    body("No order available for the requested id");
        }
            }

    /**
     * |Method to get the least sold itemsa and to selling oitems
      * @param reportType type of report the admin wants to see
     * @return
     */
    @GetMapping(path = reportPath
            ,produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> getProductStats
            (@Valid @RequestParam (name="reportType") ReportingType reportType)
    {
        String correlationId = UUID.randomUUID().toString();
        log.debug("Request the report type: {} for the trace: {}",reportType, correlationId);
       return switch (reportType){
           case TOPSELLING ->
                 ResponseEntity.ok( adminService.getTopSellingProducts());
            case LEASTSELLING ->
                 ResponseEntity.ok( adminService.getLeastSellingProducts());

       };

    }


    }



