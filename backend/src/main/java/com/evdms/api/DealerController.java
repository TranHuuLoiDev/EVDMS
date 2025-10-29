package com.evdms.api;
import com.evdms.domain.*;
import com.evdms.service.ContractService;
import com.evdms.service.DebtService;
import com.evdms.service.DealerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/dealer")
public class DealerController {
    private final DealerService dealerService;
    private final ContractService contractService;
    private final DebtService debtService;

    public DealerController(DealerService dealerService, ContractService contractService, DebtService debtService) {
        this.dealerService = dealerService;
        this.contractService = contractService;
        this.debtService = debtService;
    }

    @PostMapping
    public ResponseEntity<Dealer> createDealer(@RequestBody Dealer d) {
        return ResponseEntity.ok(dealerService.create(d));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dealer> updateDealer(@PathVariable Long id, @RequestBody Dealer d) {
        return ResponseEntity.ok(dealerService.update(id, d));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dealer> getDealer(@PathVariable Long id) {
        return ResponseEntity.ok(dealerService.get(id));
    }

    @GetMapping
    public ResponseEntity<List<Dealer>> list() {
        return ResponseEntity.ok(dealerService.list());
    }

    // Contracts
    @PostMapping("/{dealerId}/contracts")
    public ResponseEntity<DealerContract> addContract(@PathVariable Long dealerId, @RequestBody DealerContract c) {
        return ResponseEntity.ok(contractService.create(dealerId, c));
    }

    @GetMapping("/{dealerId}/contracts")
    public ResponseEntity<List<DealerContract>> getContracts(@PathVariable Long dealerId) {
        return ResponseEntity.ok(contractService.getByDealer(dealerId));
    }

    // Debts
    @PostMapping("/{dealerId}/debts")
    public ResponseEntity<Debt> addDebt(@PathVariable Long dealerId, @RequestBody Debt debt) {
        return ResponseEntity.ok(debtService.addDebt(dealerId, debt));
    }

    @PostMapping("/debts/{debtId}/pay")
    public ResponseEntity<Debt> payDebt(@PathVariable Long debtId, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(debtService.applyPayment(debtId, amount));
    }

    @GetMapping("/{dealerId}/debts/outstanding")
    public ResponseEntity<List<Debt>> outstanding(@PathVariable Long dealerId) {
        return ResponseEntity.ok(debtService.listOutstanding(dealerId));
    }
}