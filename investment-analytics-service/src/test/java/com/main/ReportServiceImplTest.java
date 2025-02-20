package com.main;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.main.dto.BudgetDto;
import com.main.dto.DebtDto;
import com.main.dto.TransactionDto;
import com.main.exception.BudgetNotFoundException;
import com.main.exception.DebtNotFoundException;
import com.main.exception.TransactionNotFoundException;
import com.main.proxy.FinanceClient;
import com.main.service.ReportServiceImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class ReportServiceImplTest {

    @InjectMocks
    private ReportServiceImpl reportService;

    @Mock
    private FinanceClient financeClient;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateIncomeExpenseReport_Success() {
        TransactionDto transaction = new TransactionDto(); // populate as needed
        when(financeClient.getTransactionsByUserId(1)).thenReturn(List.of(transaction));

        List<TransactionDto> result = reportService.generateIncomeExpenseReport(1);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGenerateIncomeExpenseReport_NoTransactions() {
        when(financeClient.getTransactionsByUserId(2)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(TransactionNotFoundException.class, () -> {
            reportService.generateIncomeExpenseReport(2);
        });
        assertEquals("No transactions found for user ID: 2", exception.getMessage());
    }

    @Test
    void testGetBudgetReport_Success() {
        BudgetDto budget = new BudgetDto(); // populate as needed
        when(financeClient.getBudgetReport(1, LocalDate.now(), LocalDate.now())).thenReturn(budget);

        BudgetDto result = reportService.getBudgetReport(1, LocalDate.now(), LocalDate.now());
        assertNotNull(result);
    }

    @Test
    void testGetBudgetReport_NoBudget() {
        when(financeClient.getBudgetReport(2, LocalDate.now(), LocalDate.now())).thenReturn(null);

        Exception exception = assertThrows(BudgetNotFoundException.class, () -> {
            reportService.getBudgetReport(2, LocalDate.now(), LocalDate.now());
        });
        assertEquals("No budget report found for user ID: 2", exception.getMessage());
    }

    @Test
    void testGetDebtsReport_Success() {
        DebtDto debt = new DebtDto(); // populate as needed
        when(financeClient.getDebtsByUserId(1)).thenReturn(List.of(debt));

        List<DebtDto> result = reportService.getDebtsReport(1);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetDebtsReport_NoDebts() {
        when(financeClient.getDebtsByUserId(2)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(DebtNotFoundException.class, () -> {
            reportService.getDebtsReport(2);
        });
        assertEquals("No debts found for user ID: 2", exception.getMessage());
    }
}
