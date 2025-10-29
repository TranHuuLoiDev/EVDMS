package com.evdms.repo;
import com.evdms.domain.Debt;
import com.evdms.domain.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DebtRepository extends JpaRepository<Debt, Long> {
    List<Debt> findByDealerAndStatusIn(Dealer dealer, List<String> statuses);
}
