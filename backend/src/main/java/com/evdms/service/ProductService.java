package com.evdms.service;
import com.evdms.domain.Product;
import com.evdms.repo.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository repo;
    public ProductService(ProductRepository repo) { this.repo = repo; }

    public Product create(Product p) {
        repo.findBySku(p.getSku()).ifPresent(existing -> {
            throw new IllegalArgumentException("SKU đã tồn tại: " + p.getSku());
        });
        return repo.save(p);
    }

    public Product update(Long id, Product update) {
        Product p = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        p.setName(update.getName());
        p.setDescription(update.getDescription());
        p.setBasePrice(update.getBasePrice());
        p.setUpdatedAt(java.time.Instant.now());
        return repo.save(p);
    }

    public Optional<Product> getById(Long id) { return repo.findById(id); }
    public List<Product> listAll() { return repo.findAll(); }

    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new IllegalArgumentException("Product not found");
        repo.deleteById(id);
    }
}