package com.evdms.service;
import com.evdms.domain.Debt;
import com.evdms.domain.Dealer;
import com.evdms.repo.DebtRepository;
import com.evdms.repo.DealerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DebtService {
    private final DebtRepository repo;
    private final DealerRepository dealerRepo;

    public DebtService(DebtRepository repo, DealerRepository dealerRepo) {
        this.repo = repo; this.dealerRepo = dealerRepo;
    }

    @Transactional
    public Debt addDebt(Long dealerId, Debt debt) {
        Dealer d = dealerRepo.findById(dealerId).orElseThrow(() -> new IllegalArgumentException("Dealer not found"));
        debt.setDealer(d);
        debt.setStatus("OUTSTANDING");
        return repo.save(debt);
    }

    @Transactional
    public Debt applyPayment(Long debtId, BigDecimal payment) {
        Debt debt = repo.findById(debtId).orElseThrow(() -> new IllegalArgumentException("Debt not found"));
        BigDecimal remaining = debt.getAmount().subtract(payment);
        if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
            debt.setAmount(BigDecimal.ZERO);
            debt.setStatus("PAID");
        } else {
            debt.setAmount(remaining);
            debt.setStatus("PARTIAL");
        }
        return repo.save(debt);
    }

    public List<Debt> listOutstanding(Long dealerId) {
        Dealer d = dealerRepo.findById(dealerId).orElseThrow(() -> new IllegalArgumentException("Dealer not found"));
        return repo.findByDealerAndStatusIn(d, List.of("OUTSTANDING", "PARTIAL", "OVERDUE"));
    }
}
