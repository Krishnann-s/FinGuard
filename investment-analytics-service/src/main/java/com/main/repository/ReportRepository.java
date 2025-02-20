package com.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.entity.Report;

public interface ReportRepository extends JpaRepository<Report,Integer > {
	

}
