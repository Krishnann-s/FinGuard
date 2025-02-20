package com.main.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetReportRequest {
	private int userId;
	private LocalDate startDate;
	private LocalDate endDate;
}
