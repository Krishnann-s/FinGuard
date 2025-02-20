package com.main.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BudgetResponse {
	private BigDecimal amount;
	private BigDecimal expense;
}
