package com.evdms.service;

import com.evdms.domain.Product;
import com.evdms.domain.WholesalePrice;
import com.evdms.repo.ProductRepository;
import com.evdms.repo.WholesalePriceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class WholesalePriceService {
    private final WholesalePriceRepository repo;
    private final ProductRepository productRepo;

    public WholesalePriceService(WholesalePriceRepository repo, ProductRepository productRepo) {
        this.repo = repo;
        this.productRepo = productRepo;
    }

    public Optional<WholesalePrice> getEffectivePrice(Long productId, int qty) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return repo.findValidPrices(product, qty, LocalDate.now())
                   .stream()
                   .findFirst(); // Giá có minQuantity lớn nhất phù hợp
    }
}
