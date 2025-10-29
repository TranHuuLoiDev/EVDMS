package com.evdms.repo;
import com.evdms.domain.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DealerRepository extends JpaRepository<Dealer, Long> {
    Optional<Dealer> findByCode(String code);
}
