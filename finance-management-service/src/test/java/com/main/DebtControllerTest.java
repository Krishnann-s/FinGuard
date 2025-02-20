package com.main;

import com.main.controller.DebtController;
import com.main.dto.DebtRequest;
import com.main.entity.Debt;
import com.main.exception.DebtException;
import com.main.service.DebtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DebtControllerTest {

    @InjectMocks
    private DebtController debtController;

    @Mock
    private DebtService debtService;

    private DebtRequest validDebtRequest;
    private Debt validDebt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validDebtRequest = new DebtRequest(); // Populate with valid data
        validDebt = new Debt(); // Populate with valid data
        validDebt.setLoanId(1);
    }

    @Test
    void testCreateDebt() {
        when(debtService.createDebt(validDebtRequest)).thenReturn(validDebt);
        ResponseEntity<Debt> response = debtController.createDebt(validDebtRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(validDebt.getLoanId(), response.getBody().getLoanId());
        verify(debtService).createDebt(validDebtRequest);
    }

    @Test
    void testGetDebtById() {
        when(debtService.getDebtById(1)).thenReturn(validDebt);
        ResponseEntity<Debt> response = debtController.getDebtById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(validDebt.getLoanId(), response.getBody().getLoanId());
        verify(debtService).getDebtById(1);
    }

    @Test
    void testDeleteDebt() {
        doNothing().when(debtService).deleteDebt(1);
        ResponseEntity<Void> response = debtController.deleteDebt(1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(debtService).deleteDebt(1);
    }

    @Test
    void testGetDebtsByUserId() {
        when(debtService.getDebtsByUserId(1)).thenReturn(Arrays.asList(validDebt));
        ResponseEntity<List<Debt>> response = debtController.getDebtsByUserId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(validDebt.getLoanId(), response.getBody().get(0).getLoanId());
        verify(debtService).getDebtsByUserId(1);
    }

    @Test
    void testCreateDebt_InvalidInput() {
        DebtRequest invalidRequest = new DebtRequest(); // Populate with invalid data
        when(debtService.createDebt(invalidRequest)).thenThrow(new DebtException("Invalid data"));

        ResponseEntity<Debt> response = debtController.createDebt(invalidRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetDebtById_NotFound() {
        when(debtService.getDebtById(99)).thenThrow(new DebtException("Debt not found"));

        ResponseEntity<Debt> response = debtController.getDebtById(99);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteDebt_NotFound() {
        doThrow(new DebtException("Debt not found")).when(debtService).deleteDebt(99);

        ResponseEntity<Void> response = debtController.deleteDebt(99);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetDebtsByUserId_UserNotFound() {
        when(debtService.getDebtsByUserId(99)).thenThrow(new DebtException("User not found"));

        ResponseEntity<List<Debt>> response = debtController.getDebtsByUserId(99);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
