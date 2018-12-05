package com.eximbills.backservice2.repository;

import com.eximbills.backservice2.domain.Balance;
import org.springframework.data.repository.CrudRepository;

public interface BalanceRepository extends CrudRepository<Balance, Long> {
}
