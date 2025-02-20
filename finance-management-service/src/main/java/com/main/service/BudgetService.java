 package com.main.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.main.dto.BudgetReportRequest;
import com.main.dto.BudgetResponse;
import com.main.entity.Budget;


public interface BudgetService {
//		List<Budget> getBudgetForUser(int userId);
		Budget createBudgetService(Budget budgetDto);
	    Budget getBudgetByIdService(int budgetId);
	    Budget updateBudgetService(int budgetId, Budget budgetDto);
	    BigDecimal getRemainingAmountService(int budgetId); 
	    void deleteBudgetService(int budgetId);
	    Budget getUserBudgetsService(int userId);
	    BudgetResponse getBudgetReport(BudgetReportRequest budgetReportRequest);
}