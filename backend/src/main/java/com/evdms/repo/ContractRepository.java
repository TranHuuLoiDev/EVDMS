package com.evdms.repo;
import com.evdms.domain.DealerContract;
import com.evdms.domain.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<DealerContract, Long> {
    List<DealerContract> findByDealer(Dealer dealer);
}
