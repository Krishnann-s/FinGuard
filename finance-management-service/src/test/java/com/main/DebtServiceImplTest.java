package com.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.main.dto.DebtRequest;
import com.main.dto.User;
import com.main.entity.Debt;
import com.main.exception.DebtException;
import com.main.exception.UserNotFoundException;
import com.main.proxy.UserClient;
import com.main.repository.DebtRepository;
import com.main.service.DebtServiceImpl;

public class DebtServiceImplTest {

    @InjectMocks
    private DebtServiceImpl debtService;

    @Mock
    private DebtRepository debtRepository;

    @Mock
    private UserClient userClient;

    private DebtRequest debtRequest;
    private Debt debt;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        debtRequest = new DebtRequest();
        debtRequest.setUserId(1);
        debtRequest.setLoanType("Personal Loan");
        debtRequest.setPrincipalAmount(BigDecimal.valueOf(10000));
        debtRequest.setInterestRate(Double.valueOf(5));
        debtRequest.setEmiAmount(BigDecimal.valueOf(2000));
        debtRequest.setAmountPaid(BigDecimal.ZERO);
        debtRequest.setStartDate(Date.valueOf("2023-01-01").toLocalDate());
        debtRequest.setEndDate(Date.valueOf("2024-01-01").toLocalDate());

        debt = new Debt();
        debt.setUserId(1);
        debt.setLoanType("Personal Loan");
        debt.setPrincipalAmount(BigDecimal.valueOf(10000));
        debt.setInterestRate(Double.valueOf(5));
        debt.setEmiAmount(BigDecimal.valueOf(2000));
        debt.setAmountPaid(BigDecimal.ZERO);
        debt.setStartDate(Date.valueOf("2023-01-01").toLocalDate());
        debt.setEndDate(Date.valueOf("2024-01-01").toLocalDate());
    }

    @Test
    public void testCreateDebt_UserNotFound() {
        when(userClient.getUserById(debtRequest.getUserId())).thenReturn(null);
        
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            debtService.createDebt(debtRequest);
        });
        assertEquals("User ID not found: 1", exception.getMessage());
    }

    @Test
    public void testCreateDebt_Success() {
        // Create a mock User object
        User mockUser = new User();
        mockUser.setUserId(debtRequest.getUserId());
        when(userClient.getUserById(debtRequest.getUserId())).thenReturn(mockUser);
        when(debtRepository.save(any(Debt.class))).thenReturn(debt);
        Debt createdDebt = debtService.createDebt(debtRequest);
        assertNotNull(createdDebt);
        assertEquals(debt.getUserId(), createdDebt.getUserId());
    }

    @Test
    public void testGetDebtById_NotFound() {
        when(debtRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(DebtException.class, () -> {
            debtService.getDebtById(1);
        });
        assertEquals("Debt not found", exception.getMessage());
    }

    @Test
    public void testGetDebtById_Success() {
        when(debtRepository.findById(1)).thenReturn(Optional.of(debt));

        Debt foundDebt = debtService.getDebtById(1);
        assertEquals(debt.getUserId(), foundDebt.getUserId());
    }

    @Test
    public void testUpdateDebt_NotFound() {
        when(debtRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(DebtException.class, () -> {
            debtService.updateDebt(1, BigDecimal.valueOf(500));
        });
        assertEquals("Debt not found", exception.getMessage());
    }

    @Test
    public void testUpdateDebt_Success() {
        when(debtRepository.findById(1)).thenReturn(Optional.of(debt));
        when(debtRepository.save(any(Debt.class))).thenReturn(debt);

        Debt updatedDebt = debtService.updateDebt(1, BigDecimal.valueOf(500));
        assertEquals(BigDecimal.valueOf(500), updatedDebt.getAmountPaid());
    }

    @Test
    public void testDeleteDebt_Success() {
        doNothing().when(debtRepository).deleteById(1);
        debtService.deleteDebt(1);
        verify(debtRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteDebt_Failure() {
        doThrow(new RuntimeException("Delete failed")).when(debtRepository).deleteById(1);
        
        Exception exception = assertThrows(DebtException.class, () -> {
            debtService.deleteDebt(1);
        });
        assertEquals("Failed to delete debt", exception.getMessage());
    }

    @Test
    public void testGetDebtsByUserId_Success() {
        List<Debt> debts = new ArrayList<>();
        debts.add(debt);
        when(debtRepository.findByUserId(1)).thenReturn(debts);

        List<Debt> result = debtService.getDebtsByUserId(1);
        assertEquals(1, result.size());
        assertEquals(debt.getUserId(), result.get(0).getUserId());
    }

    @Test
    public void testGetDebtsByUserId_Failure() {
        when(debtRepository.findByUserId(1)).thenThrow(new RuntimeException("Fetch failed"));

        Exception exception = assertThrows(DebtException.class, () -> {
            debtService.getDebtsByUserId(1);
        });
        assertEquals("Failed to fetch debts", exception.getMessage());
    }
}
