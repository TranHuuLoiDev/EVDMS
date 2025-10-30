package com.evdealer.customer.service;

import com.evdealer.customer.entity.Customer;
import com.evdealer.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) { this.repo = repo; }

    public List<Customer> getAll() { return repo.findAll(); }
    public Optional<Customer> getById(Long id) { return repo.findById(id); }
    public Customer create(Customer c) { return repo.save(c); }
    public Customer update(Long id, Customer c) {
        c.setCustomer_id(id);
        return repo.save(c);
    }
    public void delete(Long id) { repo.deleteById(id); }
}
