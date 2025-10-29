package com.evdms.service;

import com.evdms.domain.Dealer;
import com.evdms.domain.DealerContract;
import com.evdms.repo.ContractRepository;
import com.evdms.repo.DealerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractService {
    private final ContractRepository repo;
    private final DealerRepository dealerRepo;

    public ContractService(ContractRepository repo, DealerRepository dealerRepo) {
        this.repo = repo;
        this.dealerRepo = dealerRepo;
    }

    public DealerContract create(Long dealerId, DealerContract c) {
        Dealer dealer = dealerRepo.findById(dealerId).orElseThrow(() -> new IllegalArgumentException("Dealer not found"));
        c.setDealer(dealer);
        c.setStatus("ACTIVE");
        return repo.save(c);
    }

    public List<DealerContract> getByDealer(Long dealerId) {
        Dealer dealer = dealerRepo.findById(dealerId).orElseThrow(() -> new IllegalArgumentException("Dealer not found"));
        return repo.findByDealer(dealer);
    }

    public DealerContract update(Long id, DealerContract update) {
        DealerContract c = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Contract not found"));
        c.setStartDate(update.getStartDate());
        c.setEndDate(update.getEndDate());
        c.setTerms(update.getTerms());
        c.setStatus(update.getStatus());
        return repo.save(c);
    }
}