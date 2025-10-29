package com.evdms.service;

import com.evdms.domain.InventoryItem;
import com.evdms.domain.Product;
import com.evdms.repo.InventoryRepository;
import com.evdms.repo.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepo;
    private final ProductRepository productRepo;

    public InventoryService(InventoryRepository inventoryRepo, ProductRepository productRepo) {
        this.inventoryRepo = inventoryRepo;
        this.productRepo = productRepo;
    }

    public List<InventoryItem> listInventory() { return inventoryRepo.findAll(); }

    public InventoryItem getById(Long id) { return inventoryRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Inventory item not found")); }

    @Transactional
    public InventoryItem adjustStock(Long productId, String location, long delta, boolean allowNegative) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        InventoryItem it = inventoryRepo.findByProductAndLocation(product, location)
                .orElseGet(() -> {
                    InventoryItem ni = new InventoryItem();
                    ni.setProduct(product);
                    ni.setLocation(location);
                    ni.setQuantity(0L);
                    ni.setReservedQuantity(0L);
                    return ni;
                });
        long newQty = it.getQuantity() + delta;
        if (!allowNegative && newQty < 0) {
            throw new IllegalStateException("Not enough stock for product " + product.getSku());
        }
        it.setQuantity(newQty);
        return inventoryRepo.save(it);
    }

    public List<InventoryItem> findByProduct(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return inventoryRepo.findByProduct(product);
    }
}
