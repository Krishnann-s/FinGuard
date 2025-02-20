package com.main.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Report {
	@Id
	@GeneratedValue
	private int reportId;
	private int userId;
	private String reportType;
	private LocalDate reportDate;
	private LocalDate generatedAt;
}
