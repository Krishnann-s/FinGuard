package com.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.entity.Portfolio;

public interface PortfolioRepository extends JpaRepository <Portfolio, Integer>{
	List<Portfolio> findByUserId(int userId);
}
