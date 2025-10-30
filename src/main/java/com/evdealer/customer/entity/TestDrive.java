package com.evdealer.customer.entity;

import jakarta.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name = "testdrives")
public class TestDrive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long test_drive_id;

    private Long customer_id;
    private Long vehicle_id;
    private Long dealer_id;

    private Date preferred_date;
    private Time preferred_time;
    private String status;
    private String note;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp created_at;

    // getters and setters
    public Long getTest_drive_id() { return test_drive_id; }
    public void setTest_drive_id(Long test_drive_id) { this.test_drive_id = test_drive_id; }

    public Long getCustomer_id() { return customer_id; }
    public void setCustomer_id(Long customer_id) { this.customer_id = customer_id; }

    public Long getVehicle_id() { return vehicle_id; }
    public void setVehicle_id(Long vehicle_id) { this.vehicle_id = vehicle_id; }

    public Long getDealer_id() { return dealer_id; }
    public void setDealer_id(Long dealer_id) { this.dealer_id = dealer_id; }

    public Date getPreferred_date() { return preferred_date; }
    public void setPreferred_date(Date preferred_date) { this.preferred_date = preferred_date; }

    public Time getPreferred_time() { return preferred_time; }
    public void setPreferred_time(Time preferred_time) { this.preferred_time = preferred_time; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Timestamp getCreated_at() { return created_at; }
    public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }
}
