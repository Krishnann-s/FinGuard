package com.main.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.main.dto.BudgetDto;
import com.main.dto.DebtDto;
import com.main.dto.TransactionDto;
import com.main.entity.Portfolio;
import com.main.service.PortfolioService;
import com.main.service.ReportService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @Autowired
    private PortfolioService portfolioService;

    // Debt Report
    @GetMapping("/financereport/{userId}")
    public ResponseEntity<List<DebtDto>> getDebtsReport(@PathVariable int userId) {
        logger.info("Fetching debts report for user ID: {}", userId);
        
        List<DebtDto> reports = reportService.getDebtsReport(userId);
        if (reports != null) {
            logger.info("Debts report found for user ID: {}", userId);
            return new ResponseEntity<>(reports, HttpStatus.OK);
        } else {
            logger.warn("No debts report found for user ID: {}", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // User Transaction Report
    @GetMapping("/incomexpensereport/{userId}")
    public ResponseEntity<List<TransactionDto>> getIncomeExpenseReport(@PathVariable int userId) {
        logger.info("Generating income and expense report for user ID: {}", userId);
        
        List<TransactionDto> reports = reportService.generateIncomeExpenseReport(userId);
        if (reports != null && !reports.isEmpty()) {
            logger.info("Income and expense report found for user ID: {}", userId);
            return new ResponseEntity<>(reports, HttpStatus.OK);
        } else {
            logger.warn("No income and expense report found for user ID: {}", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Budget Report
    @GetMapping("/budgetreport/{userId}/{startDate}/{endDate}")
    public ResponseEntity<BudgetDto> getBudgetReport(@PathVariable int userId, 
                                                      @PathVariable LocalDate startDate, 
                                                      @PathVariable LocalDate endDate) {
        logger.info("Fetching budget report for user ID: {} from {} to {}", userId, startDate, endDate);
        
        BudgetDto report = reportService.getBudgetReport(userId, startDate, endDate);
        if (report != null) {
            logger.info("Budget report found for user ID: {}", userId);
            return new ResponseEntity<>(report, HttpStatus.OK);
        } else {
            logger.warn("No budget report found for user ID: {}", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Portfolio Report
    @GetMapping("/portfolioreport/{userId}")
    public ResponseEntity<List<Portfolio>> getPortfolioReport(@PathVariable int userId) {
        logger.info("Fetching portfolio report for user ID: {}", userId);
        
        List<Portfolio> report = portfolioService.viewAllPortfolios(userId);
        if (report != null) {
            logger.info("Portfolio report found for user ID: {}", userId);
            return new ResponseEntity<>(report, HttpStatus.OK);
        } else {
            logger.warn("No portfolio report found for user ID: {}", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
