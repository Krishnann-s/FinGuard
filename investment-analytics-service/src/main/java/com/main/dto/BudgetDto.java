package com.main.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BudgetDto {
	private BigDecimal amount;
	private BigDecimal expense;
}
