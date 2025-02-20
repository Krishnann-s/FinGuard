package com.main;

import com.main.controller.ReportController;
import com.main.dto.BudgetDto;
import com.main.dto.DebtDto;
import com.main.dto.TransactionDto;
import com.main.entity.Portfolio;
import com.main.service.PortfolioService;
import com.main.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReportControllerTest {

    @InjectMocks
    private ReportController reportController;

    @Mock
    private ReportService reportService;

    @Mock
    private PortfolioService portfolioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Positive Test Cases
    @Test
    public void testGetDebtsReport_Success() {
        int userId = 1;
        List<DebtDto> debts = new ArrayList<>();
        debts.add(new DebtDto());

        when(reportService.getDebtsReport(userId)).thenReturn(debts);

        ResponseEntity<List<DebtDto>> response = reportController.getDebtsReport(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(debts, response.getBody());
    }

    @Test
    public void testGetIncomeExpenseReport_Success() {
        int userId = 1;
        List<TransactionDto> transactions = new ArrayList<>();
        transactions.add(new TransactionDto());

        when(reportService.generateIncomeExpenseReport(userId)).thenReturn(transactions);

        ResponseEntity<List<TransactionDto>> response = reportController.getIncomeExpenseReport(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
    }

    @Test
    public void testGetBudgetReport_Success() {
        int userId = 1;
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();
        BudgetDto budgetDto = new BudgetDto();

        when(reportService.getBudgetReport(userId, startDate, endDate)).thenReturn(budgetDto);

        ResponseEntity<BudgetDto> response = reportController.getBudgetReport(userId, startDate, endDate);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budgetDto, response.getBody());
    }

    @Test
    public void testGetPortfolioReport_Success() {
        int userId = 1;
        List<Portfolio> portfolios = new ArrayList<>();
        portfolios.add(new Portfolio());

        when(portfolioService.viewAllPortfolios(userId)).thenReturn(portfolios);

        ResponseEntity<List<Portfolio>> response = reportController.getPortfolioReport(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(portfolios, response.getBody());
    }

    // Negative Test Cases
    @Test
    public void testGetDebtsReport_UserNotFound() {
        int userId = 999; // Invalid user ID

        when(reportService.getDebtsReport(userId)).thenReturn(null);

        ResponseEntity<List<DebtDto>> response = reportController.getDebtsReport(userId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetIncomeExpenseReport_NoTransactions() {
        int userId = 1;

        when(reportService.generateIncomeExpenseReport(userId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<TransactionDto>> response = reportController.getIncomeExpenseReport(userId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetBudgetReport_NoBudgetAvailable() {
        int userId = 1;
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();

        when(reportService.getBudgetReport(userId, startDate, endDate)).thenReturn(null);

        ResponseEntity<BudgetDto> response = reportController.getBudgetReport(userId, startDate, endDate);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetPortfolioReport_UserNotFound() {
        int userId = 999; // Invalid user ID

        when(portfolioService.viewAllPortfolios(userId)).thenReturn(null);

        ResponseEntity<List<Portfolio>> response = reportController.getPortfolioReport(userId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
