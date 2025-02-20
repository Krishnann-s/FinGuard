package com.main;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.dto.TransactionRequest;
import com.main.dto.PortfolioDto;
import com.main.controller.TransactionController;
import com.main.dto.DebtTxn;
import com.main.entity.Transaction;
import com.main.exception.TransactionException;
import com.main.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

public class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    // Positive Test Cases
    @Test
    public void testCreateTransaction_Success() throws Exception {
        Transaction transaction = new Transaction(); // populate with valid data
        when(transactionService.createTransaction(any())).thenReturn(transaction);

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(transaction)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetTransactionById_Success() throws Exception {
        Transaction transaction = new Transaction(); // populate with valid data
        when(transactionService.getTransactionById(1)).thenReturn(transaction);

        mockMvc.perform(get("/transactions/1/userName"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTransactionsByUserId_Success() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        when(transactionService.getTransactionsByUserId(1)).thenReturn(transactions);

        mockMvc.perform(get("/transactions/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testMakeTransaction_Success() throws Exception {
        TransactionRequest txn = new TransactionRequest(); // populate with valid data
        when(transactionService.makeTransaction(any())).thenReturn("Transaction Successful");

        mockMvc.perform(post("/transactions/user/txn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(txn)))
                .andExpect(status().isOk());
    }

    @Test
    public void testPortfolioTransaction_Success() throws Exception {
        PortfolioDto portfolioDto = new PortfolioDto(); // populate with valid data
        doNothing().when(transactionService).portfolioTransaction(any());

        mockMvc.perform(post("/transactions/user/portfolio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(portfolioDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDebtTransaction_Success() throws Exception {
        DebtTxn debtTxn = new DebtTxn(); // populate with valid data
        doNothing().when(transactionService).debtTransaction(any());

        mockMvc.perform(post("/transactions/user/debt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(debtTxn)))
                .andExpect(status().isOk());
    }

    // Negative Test Cases
    @Test
    public void testCreateTransaction_NullTransaction() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetTransactionById_NotFound() throws Exception {
        when(transactionService.getTransactionById(1)).thenThrow(new TransactionException("Not Found"));

        mockMvc.perform(get("/transactions/1/userName"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetTransactionsByUserId_NoContent() throws Exception {
        when(transactionService.getTransactionsByUserId(1)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/transactions/user/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testMakeTransaction_NullRequest() throws Exception {
        mockMvc.perform(post("/transactions/user/txn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPortfolioTransaction_InvalidData() throws Exception {
        PortfolioDto portfolioDto = new PortfolioDto(); // empty or invalid data
        mockMvc.perform(post("/transactions/user/portfolio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(portfolioDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDebtTransaction_NullDebt() throws Exception {
        mockMvc.perform(post("/transactions/user/debt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    // Exception Handling
    @Test
    public void testCreateTransaction_ExceptionHandling() throws Exception {
        Transaction transaction = new Transaction(); // valid data
        when(transactionService.createTransaction(any())).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(transaction)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetTransactionById_ExceptionHandling() throws Exception {
        when(transactionService.getTransactionById(1)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/transactions/1/userName"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetTransactionsByUserId_ExceptionHandling() throws Exception {
        when(transactionService.getTransactionsByUserId(1)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/transactions/user/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testMakeTransaction_ExceptionHandling() throws Exception {
        TransactionRequest txn = new TransactionRequest(); // valid data
        when(transactionService.makeTransaction(any())).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/transactions/user/txn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(txn)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testPortfolioTransaction_ExceptionHandling() throws Exception {
        PortfolioDto portfolioDto = new PortfolioDto(); // valid data
        doThrow(new RuntimeException("Error")).when(transactionService).portfolioTransaction(any());

        mockMvc.perform(post("/transactions/user/portfolio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(portfolioDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testDebtTransaction_ExceptionHandling() throws Exception {
        DebtTxn debtTxn = new DebtTxn(); // valid data
        doThrow(new RuntimeException("Error")).when(transactionService).debtTransaction(any());

        mockMvc.perform(post("/transactions/user/debt")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(debtTxn)))
                .andExpect(status().isInternalServerError());
    }
}
