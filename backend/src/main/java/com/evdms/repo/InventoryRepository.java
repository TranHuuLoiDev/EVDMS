package com.evdms.repo;
import com.evdms.domain.InventoryItem;
import com.evdms.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findByProductAndLocation(Product product, String location);
    List<InventoryItem> findByProduct(Product product);
}
