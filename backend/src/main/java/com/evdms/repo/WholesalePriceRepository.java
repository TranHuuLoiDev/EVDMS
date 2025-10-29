package com.evdms.repo;

import com.evdms.domain.Product;
import com.evdms.domain.WholesalePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface WholesalePriceRepository extends JpaRepository<WholesalePrice, Long> {

    @Query("""
        SELECT w FROM WholesalePrice w
        WHERE w.product = :product
          AND (w.effectiveFrom IS NULL OR w.effectiveFrom <= :today)
          AND (w.effectiveTo IS NULL OR w.effectiveTo >= :today)
          AND w.minQuantity <= :qty
        ORDER BY w.minQuantity DESC
    """)
    List<WholesalePrice> findValidPrices(Product product, int qty, LocalDate today);
}
