package com.evdms.service;
import com.evdms.domain.Dealer;
import com.evdms.repo.DealerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DealerService {
    private final DealerRepository repo;
    public DealerService(DealerRepository repo) { this.repo = repo; }

    public Dealer create(Dealer d) {
        repo.findByCode(d.getCode()).ifPresent(ex -> { throw new IllegalArgumentException("Dealer code exists"); });
        d.setStatus("ACTIVE");
        return repo.save(d);
    }

    public Dealer update(Long id, Dealer update) {
        Dealer d = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Dealer not found"));
        d.setName(update.getName());
        d.setContactPerson(update.getContactPerson());
        d.setPhone(update.getPhone());
        d.setEmail(update.getEmail());
        d.setAddress(update.getAddress());
        d.setStatus(update.getStatus());
        return repo.save(d);
    }

    public Dealer get(Long id) { return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Dealer not found")); }
    public List<Dealer> list() { return repo.findAll(); }
}
