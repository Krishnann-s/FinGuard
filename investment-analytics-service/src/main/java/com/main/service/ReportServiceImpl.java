package com.main.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.dto.BudgetDto;
import com.main.dto.DebtDto;
import com.main.dto.TransactionDto;
import com.main.exception.BudgetNotFoundException;
import com.main.exception.DebtNotFoundException;
import com.main.exception.TransactionNotFoundException;
import com.main.proxy.FinanceClient;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Transactional
@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    private FinanceClient financeClient;

    @Override
    public List<TransactionDto> generateIncomeExpenseReport(int userId) {
        logger.info("Generating income and expense report for user ID: {}", userId);
        
        List<TransactionDto> transactions = financeClient.getTransactionsByUserId(userId);
        if (transactions == null || transactions.isEmpty()) {
            logger.error("No transactions found for user ID: {}", userId);
            throw new TransactionNotFoundException("No transactions found for user ID: " + userId);
        }
        
        logger.info("Found {} transactions for user ID: {}", transactions.size(), userId);
        return transactions;
    }

    public List<TransactionDto> fallbackGenerateIncomeExpenseReport(int userId, Throwable throwable) {
        logger.warn("Fallback invoked for generateIncomeExpenseReport: {}", throwable.getMessage());
        // Return an empty list or some default value
        return Collections.emptyList();
    }

    @Override
    public BudgetDto getBudgetReport(int userId, LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching budget report for user ID: {} from {} to {}", userId, startDate, endDate);
        
        BudgetDto budget = financeClient.getBudgetReport(userId, startDate, endDate);
        if (budget == null) {
            logger.error("No budget report found for user ID: {}", userId);
            throw new BudgetNotFoundException("No budget report found for user ID: " + userId);
        }
        
        logger.info("Budget report retrieved for user ID: {}", userId);
        return budget;
    }

    public BudgetDto fallbackGetBudgetReport(int userId, LocalDate startDate, LocalDate endDate, Throwable throwable) {
        logger.warn("Fallback invoked for getBudgetReport: {}", throwable.getMessage());
        
        return new BudgetDto();
    }

    @Override
    public List<DebtDto> getDebtsReport(int userId) {
        logger.info("Generating debts report for user ID: {}", userId);
        
        List<DebtDto> debt = financeClient.getDebtsByUserId(userId);
        if (debt == null || debt.isEmpty()) {
            logger.error("No debts found for user ID: {}", userId);
            throw new DebtNotFoundException("No debts found for user ID: " + userId);
        }
        
        logger.info("Found {} debts for user ID: {}", debt.size(), userId);
        return debt;
    }

    public List<DebtDto> fallbackGetDebtsReport(int userId, Throwable throwable) {
        logger.warn("Fallback invoked for getDebtsReport: {}", throwable.getMessage());
       
        return Collections.emptyList();
    }
}
