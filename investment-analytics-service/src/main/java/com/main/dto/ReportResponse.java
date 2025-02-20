package com.main.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportResponse {
	  private int userId;
	    private LocalDate startDate;
	    private LocalDate endDate;
	    private List<TransactionDto> transactions;
	    private BudgetDto budget;
	    private List<DebtDto> debts;
	    
}
