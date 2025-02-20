package com.main.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.main.dto.BudgetDto;
import com.main.dto.DebtDto;
import com.main.dto.TransactionDto;
@Service
public interface ReportService{
//	FinancialSummaryReport generateFinancialSummary(userId);
	List<TransactionDto> generateIncomeExpenseReport(int userId);
//	BudgetReport generateBudgetReport(userId);
	BudgetDto getBudgetReport(int userId, LocalDate startDate, LocalDate endDate);
//	PortfolioPerformanceReport generatePortfolioReport(userId);
//	List<FinancialGoalReport> generateFinancialGoalsReport;
//	List<HistoricalReport> fetchHistoricalReports(userId);
	
	List<DebtDto> getDebtsReport(int userId);
}