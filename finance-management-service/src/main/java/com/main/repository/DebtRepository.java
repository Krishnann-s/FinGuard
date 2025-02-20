package com.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.entity.Debt;

import lombok.Data;

public interface DebtRepository extends JpaRepository<Debt, Integer> {
	List<Debt> findByUserId(int userId);
}
