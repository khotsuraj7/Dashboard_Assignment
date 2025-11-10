
package com.example.sellerdashboard.repository;

import com.example.sellerdashboard.entity.Sale;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends CrudRepository<Sale, Long> {
    @Query("""
        SELECT s.date AS date, SUM(s.quantity) AS totalQty, SUM(s.quantity * s.price) AS revenue, 
               SUM(CASE WHEN s.returned = true THEN s.quantity ELSE 0 END) AS returnedQty
        FROM Sale s
        WHERE s.seller.id = :sellerId AND s.date BETWEEN :start AND :end
        GROUP BY s.date
    """)
    List<Object[]> aggregateDailyForRange(@Param("sellerId") Long sellerId, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
