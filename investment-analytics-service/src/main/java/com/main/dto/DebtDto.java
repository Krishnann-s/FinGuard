package com.main.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class DebtDto {
	private int userId; 
	private String loanType;
    private BigDecimal principalAmount;
    private Double interestRate;
    private BigDecimal emiAmount;
    private BigDecimal amountPaid;
    private LocalDate startDate;
    private LocalDate endDate;
}
