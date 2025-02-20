package com.main.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; // For SLF4J logging
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.dto.BudgetReportRequest;
import com.main.dto.BudgetResponse;
import com.main.entity.Budget;
import com.main.exception.BudgetException;

import com.main.service.BudgetService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private static final Logger logger = LoggerFactory.getLogger(BudgetController.class); // Add logger

    @Autowired
    private BudgetService budgetService;

    // Create a new budget
    @PostMapping
    public ResponseEntity<Budget> createBudget(@Valid @RequestBody Budget budgetDto) {
        logger.info("Creating new budget for user with ID: {}", budgetDto.getUserId()); // Log request
        Budget createdBudget = budgetService.createBudgetService(budgetDto);
        if (createdBudget == null) {
            throw new BudgetException("Failed to create budget for user with ID: " + budgetDto.getUserId());
        }
        logger.info("Successfully created budget with ID: {}", createdBudget.getUserId()); // Log success
        return new ResponseEntity<>(createdBudget, HttpStatus.CREATED);
    }

    // Get a budget by ID
    @GetMapping("/{budgetId}")
    public ResponseEntity<Budget> getBudgetById(@PathVariable int budgetId) {
        logger.info("Fetching budget with ID: {}", budgetId); // Log request
        Budget budget = budgetService.getBudgetByIdService(budgetId);
        if (budget == null) {
            throw new BudgetException("Budget ID not found: " + budgetId);
        }
        logger.info("Found budget: {}", budget); // Log result
        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    // Update a budget
    @PutMapping("/{budgetId}")
    public ResponseEntity<Budget> updateBudget(@PathVariable int budgetId, @RequestBody Budget budgetDto) {
        logger.info("Updating budget with ID: {}", budgetId); // Log request
        Budget updatedBudget = budgetService.updateBudgetService(budgetId, budgetDto);
        if (updatedBudget == null) {
            throw new BudgetException("Failed to update budget with ID: " + budgetId);
        }
        logger.info("Updated budget with ID: {}", updatedBudget.getUserId()); // Log success
        return new ResponseEntity<>(updatedBudget, HttpStatus.OK);
    }

    // Delete a budget
    @DeleteMapping("/{budgetId}")
    public ResponseEntity<Void> deleteBudget(@PathVariable int budgetId) {
        logger.info("Deleting budget with ID: {}", budgetId); // Log request
        try {
            budgetService.deleteBudgetService(budgetId);
        } catch (Exception e) {
            throw new BudgetException("Failed to delete budget with ID: " + budgetId);
        }
        logger.info("Deleted budget with ID: {}", budgetId); // Log success
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get all budgets for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<Budget> getUserBudgets(@PathVariable int userId) {
        logger.info("Fetching all budgets for user with ID: {}", userId); // Log request
        Budget budgets = budgetService.getUserBudgetsService(userId);
        if (budgets == null) {
            throw new BudgetException("No budgets found for user with ID: " + userId);
        }
        logger.info("Found budgets for user with ID: {}", userId); // Log result
        return new ResponseEntity<>(budgets, HttpStatus.OK);
    }

    // Get remaining amount in budget
    @GetMapping("/{budgetId}/remaining")
    public ResponseEntity<BigDecimal> getRemainingAmount(@PathVariable int budgetId) {
        logger.info("Fetching remaining amount for budget with ID: {}", budgetId); // Log request
        BigDecimal remainingAmount = budgetService.getRemainingAmountService(budgetId);
        if (remainingAmount == null) {
            throw new BudgetException("Failed to fetch remaining amount for budget ID: " + budgetId);
        }
        logger.info("Remaining amount for budget ID {}: {}", budgetId, remainingAmount); // Log result
        return new ResponseEntity<>(remainingAmount, HttpStatus.OK);
    }

    // Get budget report
    @GetMapping("/report/{userId}/{startDate}/{endDate}")
    public ResponseEntity<BudgetResponse> getBudgetReport(@PathVariable int userId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        logger.info("Generating budget report for user with ID: {} from {} to {}", userId, startDate, endDate); // Log request
        BudgetReportRequest budgetResponseRequest = new BudgetReportRequest();
        budgetResponseRequest.setUserId(userId);
        budgetResponseRequest.setStartDate(startDate);
        budgetResponseRequest.setEndDate(endDate);

        BudgetResponse budgetResponse = budgetService.getBudgetReport(budgetResponseRequest);
        if (budgetResponse == null) {
            throw new BudgetException("Failed to generate budget report for user ID: " + userId);
        }
        logger.info("Budget report generated for user ID: {}", userId); // Log result
        return new ResponseEntity<>(budgetResponse, HttpStatus.OK);
    }
}

