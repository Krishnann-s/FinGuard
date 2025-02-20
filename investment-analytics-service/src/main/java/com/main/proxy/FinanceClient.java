package com.main.proxy;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.main.dto.BudgetDto;
import com.main.dto.DebtDto;
import com.main.dto.PortfolioRequest;
import com.main.dto.TransactionDto;

@FeignClient("finance-management-service")
public interface FinanceClient {
	@PostMapping("/transactions/user/portfolio")
	Void portfolioTransaction(@RequestBody PortfolioRequest portfolioRequest);
	
    @GetMapping("/transactions/user/{userId}")
    List<TransactionDto> getTransactionsByUserId(@PathVariable int userId);
    
    @GetMapping("/api/budgets/report/{userId}/{startDate}/{endDate}")
    BudgetDto getBudgetReport(@PathVariable int userId, 
                               @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, 
                               @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate);    
    @GetMapping("/debts/user/{userId}")
    List<DebtDto> getDebtsByUserId(@PathVariable int userId);
 
}
