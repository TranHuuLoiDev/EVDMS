package com.evdms.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "dealer_contracts")
public class DealerContract {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Dealer dealer;

    private LocalDate startDate;
    private LocalDate endDate;
    @Lob
    private String terms;
    private String status; // ACTIVE, EXPIRED, CANCELLED

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Dealer getDealer() { return dealer; }
    public void setDealer(Dealer dealer) { this.dealer = dealer; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getTerms() { return terms; }
    public void setTerms(String terms) { this.terms = terms; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
