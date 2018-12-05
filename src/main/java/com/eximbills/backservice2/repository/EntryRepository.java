package com.eximbills.backservice2.repository;

import com.eximbills.backservice2.domain.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface EntryRepository extends CrudRepository<Entry, String> {
    Page<Entry> findByBalanceId(Long balanceId, Pageable pageable);
}
