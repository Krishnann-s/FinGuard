package com.main.service;
import java.math.BigDecimal;
import java.util.List;  

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.main.dto.BudgetReportRequest;
import com.main.dto.BudgetResponse;
import com.main.entity.Budget;
import com.main.entity.Transaction;
import com.main.exception.BudgetException;

import com.main.exception.RemainingAmountException;
import com.main.proxy.UserClient;
import com.main.repository.BudgetRepository;
import com.main.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BudgetServiceImpl implements BudgetService {

    private static final Logger logger = LoggerFactory.getLogger(BudgetServiceImpl.class);

    @Autowired
    private BudgetRepository budgetRepository;

//    @Autowired
//    private UserClient userClient;
    
    @Autowired
    private TransactionService transactionService;

    @Override
    public Budget createBudgetService(Budget budgetDto) { 
        logger.info("Creating new budget for user ID: {}", budgetDto.getUserId());
        Budget budget = new Budget();
        budget.setUserId(budgetDto.getUserId());
        budget.setAmount(budgetDto.getAmount());
        budget.setSpentAmount(budgetDto.getSpentAmount());
        budget.setCategory(budgetDto.getCategory());
        budget.setStartDate(budgetDto.getStartDate());
        budget.setEndDate(budgetDto.getEndDate());

        logger.info("Budget created successfully for user ID: {}", budgetDto.getUserId());
        return budgetRepository.save(budget);
    }

    @Override
    public Budget getBudgetByIdService(int budgetId) {
        logger.info("Fetching budget with ID: {}", budgetId);
        return budgetRepository.findById(budgetId)
                .orElseThrow(() -> {
                    logger.error("Budget not found with ID: {}", budgetId);
                    return new BudgetException("Budget not found with ID: " + budgetId);
                });
    }

    @Override
    public Budget updateBudgetService(int budgetId, Budget budgetDto) {
        logger.info("Updating budget with ID: {}", budgetId);
        Budget budget = getBudgetByIdService(budgetId); // Fetch the existing budget
        budget.setAmount(budgetDto.getAmount());
        budget.setCategory(budgetDto.getCategory());
        budget.setStartDate(budgetDto.getStartDate());
        budget.setEndDate(budgetDto.getEndDate());

        logger.info("Budget updated successfully for ID: {}", budgetId);
        return budgetRepository.save(budget);
    }

    @Override
    public void deleteBudgetService(int budgetId) {
        logger.info("Deleting budget with ID: {}", budgetId);
        Budget budget = getBudgetByIdService(budgetId);
        budgetRepository.delete(budget);
        logger.info("Budget deleted successfully for ID: {}", budgetId);
    }

    @Override
    public Budget getUserBudgetsService(int userId) {
        logger.info("Fetching budgets for user ID: {}", userId);
        return budgetRepository.findByUserId(userId);
    }
    
    public BigDecimal getRemainingAmountService(int budgetId) {
        logger.info("Calculating remaining amount for budget ID: {}", budgetId); 
        Budget budget = getBudgetByIdService(budgetId); // Fetch the budget

        // Check if spent amount exceeds total budget
        if (budget.getSpentAmount().compareTo(budget.getAmount()) > 0) {
            logger.error("Spent amount exceeds the budget for budget ID: {}", budgetId);
            throw new RemainingAmountException("Spent amount exceeds the budget.");
        }

        // Calculate the remaining amount using BigDecimal's subtract method
        BigDecimal remainingAmount = budget.getAmount().subtract(budget.getSpentAmount());
        logger.info("Remaining amount for budget ID {}: {}", budgetId, remainingAmount);
        
        return remainingAmount;
    }

		public BudgetResponse getBudgetReport(BudgetReportRequest budgetReportRequest) {
	        logger.info("Fetching budget report for user ID: {}", budgetReportRequest.getUserId());

	        // Fetch transactions for the user based on the report request
	        List<Transaction> transactions = transactionService.monthlyExpense(budgetReportRequest);
	        if (transactions == null || transactions.isEmpty()) {
	            logger.warn("No transactions found for user ID: {}", budgetReportRequest.getUserId());
	        } else {
	            logger.info("Fetched {} transactions for user ID: {}", transactions.size(), budgetReportRequest.getUserId());
	        }

	        // Fetch budget for the user
	        Budget budget = budgetRepository.findByUserId(budgetReportRequest.getUserId());
	        if (budget == null) {
	            logger.error("No budget found for user ID: {}", budgetReportRequest.getUserId());
	            throw new BudgetException("Budget not found for user ID: " + budgetReportRequest.getUserId());
	        }

	        // Calculate total expenses for 'Debited' transactions
	        double totalExpenses = transactions.stream()
	            .filter(txn -> txn.getTxnType().equalsIgnoreCase("Debited"))
	            .map(Transaction::getAmount)
	            .mapToDouble(BigDecimal::doubleValue)
	            .sum();

	        logger.info("Total expenses calculated for user ID {}: {}", budgetReportRequest.getUserId(), totalExpenses);

	        // Prepare the response
	        BudgetResponse budgetResponse = new BudgetResponse();
	        budgetResponse.setExpense(BigDecimal.valueOf(totalExpenses));
	        budgetResponse.setAmount(budget.getAmount());

	        logger.info("Budget report generated successfully for user ID: {}", budgetReportRequest.getUserId());
	        return budgetResponse;
	    
	}
}

