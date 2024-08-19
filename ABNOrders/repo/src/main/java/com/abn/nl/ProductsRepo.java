package com.abn.nl;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;



@Repository
public interface ProductsRepo extends JpaRepository<Product, Long> {
    List<Product> findProductByNameIn(List<String> name);
    Optional<Product> findProductByNameAndBrandAndSeller(String name, String brand, String seller);
    @Query(value = "SELECT p.name as name,p.stockleft as stockleft," +
            "p.price as price, COUNT(op.products_Id) as productCount  FROM products p " +
            "JOIN orders_products op ON op.products_Id=p.Id JOIN orders o ON  op.orders_Id = o.Id AND o.ordered_on=?1" +
            " GROUP BY p.Id, p.name ORDER BY productCount DESC LIMIT 5",
            nativeQuery = true)
    List<Object[]> getTopSellingProducts(@Param("day") LocalDate day);

    @Query(value = "SELECT p.name as name,p.stockleft as stockleft," +
            "p.price as price, COUNT(op.products_Id) as productCount  FROM products p " +
            "JOIN orders_products op ON op.products_Id=p.Id JOIN orders o ON  op.orders_Id = o.Id AND o.ordered_on between ?1 and ?2" +
            " GROUP BY p.Id, p.name ORDER BY productCount ASC LIMIT 5",
            nativeQuery = true)
    List<Object[]> getLeastSellingProducts(@Param("start") LocalDate start,@Param("end") LocalDate end);
}
