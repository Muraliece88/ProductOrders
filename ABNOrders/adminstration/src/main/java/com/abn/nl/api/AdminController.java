package com.abn.nl.api;

import com.abn.nl.dto.OrderDto;
import com.abn.nl.dto.ProductStatsDto;
import com.abn.nl.dto.ReportingType;
import com.abn.nl.service.AdminServiceImpl;
import com.abn.nl.validator.RepTypeConstraint;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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


    @GetMapping(path = searchOrders
            ,produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> getProducts(@PathVariable(name="orderId") String orderId)
    {
       OrderDto orderDetails= adminService.searchOrderById(orderId);
        if(Optional.ofNullable(orderDetails).isPresent()){
            return ResponseEntity.ok(orderDetails);
        }
        else{
            return ResponseEntity.status(404).
                    body("No order available for the requested id");
        }
            }
    @GetMapping(path = reportPath
            ,produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> getProductStats
            ( @RequestParam (name="reportType") ReportingType reportType)
    {

       return switch (reportType){
           case TOPSELLING ->
                 ResponseEntity.ok( adminService.getTopSellingProducts());
            case LEASTSELLING ->
                 ResponseEntity.ok( adminService.getLeastSellingProducts());

           default -> throw new BadRequestException("Unexpected value: " + reportType.name());
       };

    }


    }



