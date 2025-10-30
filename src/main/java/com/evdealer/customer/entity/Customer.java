package com.evdealer.customer.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customer_id;

    private String full_name;
    private String phone;
    private String email;
    private String address;

    @Column(name = "date_created", insertable = false, updatable = false)
    private Timestamp date_created;

    // getters and setters
    public Long getCustomer_id() { return customer_id; }
    public void setCustomer_id(Long customer_id) { this.customer_id = customer_id; }

    public String getFull_name() { return full_name; }
    public void setFull_name(String full_name) { this.full_name = full_name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Timestamp getDate_created() { return date_created; }
    public void setDate_created(Timestamp date_created) { this.date_created = date_created; }
}
