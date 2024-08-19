package com.abn.nl;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;
    private String brand;
    private int stockleft;
    private String currency;
    @Column(precision = 7, scale = 2)
    private BigDecimal price;
    private String seller;
    @ManyToMany(mappedBy = "products")
    @JsonBackReference
    private Set<Order> orders;


}
