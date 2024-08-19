package com.abn.nl;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="orders_products",joinColumns = @JoinColumn(name = "orders_id"),
            inverseJoinColumns = @JoinColumn(name="products_id"))
    @JsonManagedReference
    private Set<Product> products;
    @Column(name="ordered_on",nullable = false)
    @CreatedDate
    private LocalDate orderedOn;
    @Column(name="ordered_by")
    private String orderedBy;
    private String contactNumber;
    private int quantity;

}
