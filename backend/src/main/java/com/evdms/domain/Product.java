package com.evdms.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
@Entity
@Table(name = "products", indexes = {
        @Index(columnList = "sku", name = "idx_product_sku")
})
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal basePrice;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    private Instant updatedAt;

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
