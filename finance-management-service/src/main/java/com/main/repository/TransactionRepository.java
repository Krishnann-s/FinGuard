package com.main.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByUserId(int userId);
    List<Transaction> findByTxnDateBetween(LocalDate startDate, LocalDate endDate);
}