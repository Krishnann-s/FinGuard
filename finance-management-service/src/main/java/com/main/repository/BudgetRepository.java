package com.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.entity.Budget;
import com.main.entity.Transaction;

public interface BudgetRepository extends JpaRepository<Budget,Integer>{
	Budget findByUserId(int userId);
}
