package com.evdms.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "wholesale_prices")
public class WholesalePrice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Product product;

    private Integer minQuantity; // áp dụng khi mua >= minQuantity

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private String note;

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getMinQuantity() { return minQuantity; }
    public void setMinQuantity(Integer minQuantity) { this.minQuantity = minQuantity; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
