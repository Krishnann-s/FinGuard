package com.main;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;
import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import com.main.entity.Budget;
import com.main.exception.BudgetException;

import com.main.exception.RemainingAmountException;
import com.main.proxy.UserClient;
import com.main.repository.BudgetRepository;
import com.main.service.BudgetServiceImpl;

@SpringBootTest
class BudgetServiceImplTest {

	    @Mock
	    private BudgetRepository budgetRepository;

	    @Mock
	    private UserClient userClient;

	    @InjectMocks
	    private BudgetServiceImpl budgetService;

	    @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.initMocks(this);
	    }

	    @Test
	    public void testCreateBudgetService_Success() {
	        Budget budgetDto = new Budget();
	        budgetDto.setUserId(1);
	        budgetDto.setAmount(BigDecimal.valueOf(1000));
	        budgetDto.setSpentAmount(BigDecimal.valueOf(200));
	        budgetDto.setCategory("Food");

	        Budget budget = new Budget();
	        budget.setUserId(1);
	        budgetDto.setAmount(BigDecimal.valueOf(1000));
	        budgetDto.setSpentAmount(BigDecimal.valueOf(200));
	        budget.setCategory("Food");

	        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

	        Budget createdBudget = budgetService.createBudgetService(budgetDto);
	        assertNotNull(createdBudget);
	        assertEquals(1, createdBudget.getUserId());
	        assertEquals(1000, createdBudget.getAmount());
	        assertEquals("Food", createdBudget.getCategory());

	        verify(budgetRepository, times(1)).save(any(Budget.class));
	    }

	    @Test
	    public void testCreateBudgetService_InvalidBudgetAmount() {
	        Budget budgetDto = new Budget();
	        budgetDto.setUserId(1);
	        budgetDto.setAmount(BigDecimal.valueOf(1000));
	        budgetDto.setSpentAmount(BigDecimal.valueOf(200));

	        assertThrows(BudgetException.class, () -> {
	            budgetService.createBudgetService(budgetDto);
	        });

	        verify(budgetRepository, never()).save(any(Budget.class));
	    }

	    @Test
	    public void testGetBudgetByIdService_Success() {
	        Budget budget = new Budget();
	        budget.setUserId(1);
	        budget.setAmount(BigDecimal.valueOf(1000));
	        budget.setSpentAmount(BigDecimal.valueOf(200));

	        when(budgetRepository.findById(1)).thenReturn(Optional.of(budget));

	        Budget fetchedBudget = budgetService.getBudgetByIdService(1);
	        assertNotNull(fetchedBudget);
	        assertEquals(1, fetchedBudget.getUserId());

	        verify(budgetRepository, times(1)).findById(1);
	    }

	    @Test
	    public void testGetBudgetByIdService_BudgetNotFound() {
	        when(budgetRepository.findById(1)).thenReturn(Optional.empty());

	        assertThrows(BudgetException.class, () -> {
	            budgetService.getBudgetByIdService(1);
	        });

	        verify(budgetRepository, times(1)).findById(1);
	    }

	    @Test
	    public void testUpdateBudgetService_Success() {
	        Budget budget = new Budget();
	        budget.setUserId(1);
	        budget.setAmount(BigDecimal.valueOf(1000));

	        Budget budgetDto = new Budget();
	        budgetDto.setAmount(BigDecimal.valueOf(2000));
	        budgetDto.setCategory("Entertainment");

	        when(budgetRepository.findById(1)).thenReturn(Optional.of(budget));
	        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

	        Budget updatedBudget = budgetService.updateBudgetService(1, budgetDto);
	        assertNotNull(updatedBudget);
	        assertEquals(2000, updatedBudget.getAmount());
	        assertEquals("Entertainment", updatedBudget.getCategory());

	        verify(budgetRepository, times(1)).findById(1);
	        verify(budgetRepository, times(1)).save(any(Budget.class));
	    }
	    
	    @Test
	    public void testDeleteBudgetService_Success() {
	        Budget budget = new Budget();
	        budget.setUserId(1);

	        when(budgetRepository.findById(1)).thenReturn(Optional.of(budget));

	        budgetService.deleteBudgetService(1);

	        verify(budgetRepository, times(1)).delete(budget);
	    }

	    @Test
	    public void testGetUserBudgetsService() {
	        Budget budget1 = new Budget();
	        budget1.setUserId(1);

	        when(budgetRepository.findByUserId(1)).thenReturn(budget1);

	        Budget budget = budgetService.getUserBudgetsService(1);
	        assertNotNull(budget);
	        assertEquals(1, budget.getUserId());

	        verify(budgetRepository, times(1)).findByUserId(1);
	    }

	    @Test
	    public void testGetRemainingAmountService_Success() {
	        Budget budget = new Budget();
	        budget.setUserId(1);
	        budget.setAmount(BigDecimal.valueOf(1000));
	        budget.setSpentAmount(BigDecimal.valueOf(500));

	        when(budgetRepository.findById(1)).thenReturn(Optional.of(budget));

	        BigDecimal remainingAmount = budgetService.getRemainingAmountService(1);
	        assertEquals(500, remainingAmount);

	        verify(budgetRepository, times(1)).findById(1);
	    }

	    @Test
	    public void testGetRemainingAmountService_SpentExceedsBudget() {
	        Budget budget = new Budget();
	        budget.setUserId(1);
	        budget.setAmount(BigDecimal.valueOf(1000));
	        budget.setSpentAmount(BigDecimal.valueOf(1200)); // Spent exceeds budget

	        when(budgetRepository.findById(1)).thenReturn(Optional.of(budget));

	        assertThrows(RemainingAmountException.class, () -> {
	            budgetService.getRemainingAmountService(1);
	        });

	        verify(budgetRepository, times(1)).findById(1);
	    }

}













