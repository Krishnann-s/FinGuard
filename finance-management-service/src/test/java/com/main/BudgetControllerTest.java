package com.main;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.main.controller.BudgetController;
import com.main.dto.BudgetReportRequest;
import com.main.dto.BudgetResponse;
import com.main.entity.Budget;
import com.main.exception.BudgetException;
import com.main.service.BudgetService;

class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBudget() {
        Budget budget = new Budget();
        budget.setUserId(1);
        when(budgetService.createBudgetService(any(Budget.class))).thenReturn(budget);

        ResponseEntity<Budget> response = budgetController.createBudget(budget);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(budget, response.getBody());
        verify(budgetService, times(1)).createBudgetService(budget);
    }

    @Test
    void testCreateBudget_Exception() {
        Budget budget = new Budget();
        budget.setUserId(1);
        when(budgetService.createBudgetService(any(Budget.class))).thenReturn(null);

        BudgetException exception = assertThrows(BudgetException.class, () -> {
            budgetController.createBudget(budget);
        });

        assertEquals("Failed to create budget for user with ID: 1", exception.getMessage());
    }

    @Test
    void testGetBudgetById() {
        Budget budget = new Budget();
        budget.setBudgetId(1);
        when(budgetService.getBudgetByIdService(1)).thenReturn(budget);

        ResponseEntity<Budget> response = budgetController.getBudgetById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budget, response.getBody());
        verify(budgetService, times(1)).getBudgetByIdService(1);
    }

    @Test
    void testGetBudgetById_Exception() {
        when(budgetService.getBudgetByIdService(1)).thenReturn(null);

        BudgetException exception = assertThrows(BudgetException.class, () -> {
            budgetController.getBudgetById(1);
        });

        assertEquals("Budget ID not found: 1", exception.getMessage());
    }

    @Test
    void testUpdateBudget() {
        Budget budget = new Budget();
        budget.setUserId(1);
        when(budgetService.updateBudgetService(eq(1), any(Budget.class))).thenReturn(budget);

        ResponseEntity<Budget> response = budgetController.updateBudget(1, budget);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budget, response.getBody());
        verify(budgetService, times(1)).updateBudgetService(eq(1), any(Budget.class));
    }

    @Test
    void testUpdateBudget_Exception() {
        when(budgetService.updateBudgetService(eq(1), any(Budget.class))).thenReturn(null);

        BudgetException exception = assertThrows(BudgetException.class, () -> {
            budgetController.updateBudget(1, new Budget());
        });

        assertEquals("Failed to update budget with ID: 1", exception.getMessage());
    }

    @Test
    void testDeleteBudget() {
        doNothing().when(budgetService).deleteBudgetService(1);

        ResponseEntity<Void> response = budgetController.deleteBudget(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(budgetService, times(1)).deleteBudgetService(1);
    }

    @Test
    void testDeleteBudget_Exception() {
        doThrow(new RuntimeException()).when(budgetService).deleteBudgetService(1);

        BudgetException exception = assertThrows(BudgetException.class, () -> {
            budgetController.deleteBudget(1);
        });

        assertEquals("Failed to delete budget with ID: 1", exception.getMessage());
    }

    @Test
    void testGetUserBudgets() {
        Budget budget = new Budget();
        when(budgetService.getUserBudgetsService(1)).thenReturn(budget);

        ResponseEntity<Budget> response = budgetController.getUserBudgets(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budget, response.getBody());
        verify(budgetService, times(1)).getUserBudgetsService(1);
    }

    @Test
    void testGetUserBudgets_Exception() {
        when(budgetService.getUserBudgetsService(1)).thenReturn(null);

        BudgetException exception = assertThrows(BudgetException.class, () -> {
            budgetController.getUserBudgets(1);
        });

        assertEquals("No budgets found for user with ID: 1", exception.getMessage());
    }

    @Test
    void testGetRemainingAmount() {
        BigDecimal remainingAmount = BigDecimal.valueOf(1000);
        when(budgetService.getRemainingAmountService(1)).thenReturn(remainingAmount);

        ResponseEntity<BigDecimal> response = budgetController.getRemainingAmount(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(remainingAmount, response.getBody());
        verify(budgetService, times(1)).getRemainingAmountService(1);
    }

    @Test
    void testGetRemainingAmount_Exception() {
        when(budgetService.getRemainingAmountService(1)).thenReturn(null);

        BudgetException exception = assertThrows(BudgetException.class, () -> {
            budgetController.getRemainingAmount(1);
        });

        assertEquals("Failed to fetch remaining amount for budget ID: 1", exception.getMessage());
    }

    @Test
    void testGetBudgetReport() {
        BudgetResponse budgetResponse = new BudgetResponse();
        when(budgetService.getBudgetReport(any(BudgetReportRequest.class))).thenReturn(budgetResponse);

        ResponseEntity<BudgetResponse> response = budgetController.getBudgetReport(1, LocalDate.now(), LocalDate.now().plusDays(10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budgetResponse, response.getBody());
        verify(budgetService, times(1)).getBudgetReport(any(BudgetReportRequest.class));
    }

    @Test
    void testGetBudgetReport_Exception() {
        when(budgetService.getBudgetReport(any(BudgetReportRequest.class))).thenReturn(null);

        BudgetException exception = assertThrows(BudgetException.class, () -> {
            budgetController.getBudgetReport(1, LocalDate.now(), LocalDate.now().plusDays(10));
        });

        assertEquals("Failed to generate budget report for user ID: 1", exception.getMessage());
    }
}


























