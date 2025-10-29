package com.evdms.domain;
import jakarta.persistence.*;

@Entity
@Table(name = "inventory_items", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_id", "location"})
})
public class InventoryItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Product product;

    @Column(nullable = false)
    private Long quantity = 0L;

    private Long reservedQuantity = 0L;

    private String location; // e.g., "MainWarehouse"

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Long getQuantity() { return quantity; }
    public void setQuantity(Long quantity) { this.quantity = quantity; }
    public Long getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(Long reservedQuantity) { this.reservedQuantity = reservedQuantity; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
