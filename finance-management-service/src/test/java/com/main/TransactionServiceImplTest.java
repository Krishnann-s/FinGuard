package com.main;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.main.dto.BudgetReportRequest;
import com.main.dto.DebtTxn;
import com.main.dto.EmailResponse;
import com.main.dto.PortfolioDto;
import com.main.dto.TransactionRequest;
import com.main.dto.User;
import com.main.entity.Debt;
import com.main.entity.Transaction;
import com.main.exception.TransactionException;
import com.main.proxy.NotificationClient;
import com.main.proxy.UserClient;
import com.main.repository.TransactionRepository;
import com.main.service.DebtService;
import com.main.service.TransactionServiceImpl;

public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private DebtService debtService;

    @Mock
    private NotificationClient notificationClient;

    @Mock
    private User sender;

    @Mock
    private User receiver; 

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Positive Test Cases
    @Test
    public void testCreateTransaction() {
        Transaction transaction = new Transaction();
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        
        Transaction result = transactionService.createTransaction(transaction);
        
        assertNotNull(result);
        verify(transactionRepository).save(transaction);
    }

    @Test
    public void testGetTransactionById_Success() {
        Transaction transaction = new Transaction();
        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));
        
        Transaction result = transactionService.getTransactionById(1);
        
        assertNotNull(result);
        verify(transactionRepository).findById(1);
    }

    @Test
    public void testGetTransactionsByUserId_Success() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        when(transactionRepository.findByUserId(1)).thenReturn(transactions);
        
        List<Transaction> result = transactionService.getTransactionsByUserId(1);
        
        assertFalse(result.isEmpty());
        verify(transactionRepository).findByUserId(1);
    }

    @Test
    public void testMakeTransaction_Success() {
        TransactionRequest request = new TransactionRequest();
        request.setSenderUserId(1);
        request.setReceiverUserId(2);
        request.setAmount(BigDecimal.valueOf(100));

        when(sender.getUserId()).thenReturn(1);
        when(sender.getWallet()).thenReturn(BigDecimal.valueOf(200));
        when(receiver.getUserId()).thenReturn(2);
        when(receiver.getWallet()).thenReturn(BigDecimal.valueOf(50));
        when(userClient.getUserById(1)).thenReturn(sender);
        when(userClient.getUserById(2)).thenReturn(receiver);

        String result = transactionService.makeTransaction(request);
        
        assertEquals("Transaction successful!!", result);
        verify(transactionRepository, times(2)).save(any(Transaction.class));
        verify(notificationClient, times(2)).sendWalletUpdateEmail(any(EmailResponse.class));
    }

    @Test
    public void testDebtTransaction_Success() {
        DebtTxn debtTxn = new DebtTxn();
        debtTxn.setLoanId(1);
        debtTxn.setAmount(BigDecimal.valueOf(50));

        Debt debt = new Debt();
        debt.setUserId(1);

        when(debtService.getDebtById(1)).thenReturn(debt);
        when(userClient.getUserById(1)).thenReturn(sender);
        when(sender.getWallet()).thenReturn(BigDecimal.valueOf(100));

        transactionService.debtTransaction(debtTxn);

        verify(transactionRepository).save(any(Transaction.class));
        verify(notificationClient).sendWalletUpdateEmail(any(EmailResponse.class));
    }

    @Test
    public void testPortfolioTransaction_Success() {
        PortfolioDto portfolioDto = new PortfolioDto();
        portfolioDto.setUserId(1);
        portfolioDto.setPurchasePrice(BigDecimal.valueOf(50));

        when(userClient.getUserById(1)).thenReturn(sender);
        when(sender.getWallet()).thenReturn(BigDecimal.valueOf(100));

        transactionService.portfolioTransaction(portfolioDto);

        verify(transactionRepository).save(any(Transaction.class));
        verify(notificationClient).sendWalletUpdateEmail(any(EmailResponse.class));
    }

    @Test
    public void testMonthlyExpense() {
        BudgetReportRequest budgetReportRequest = new BudgetReportRequest();
        // Assume startDate and endDate are set
        
        List<Transaction> transactions = new ArrayList<>();
        when(transactionRepository.findByTxnDateBetween(any(), any())).thenReturn(transactions);

        List<Transaction> result = transactionService.monthlyExpense(budgetReportRequest);

        assertEquals(transactions, result);
        verify(transactionRepository).findByTxnDateBetween(any(), any());
    }

    @Test
    public void testGetTransactionById_NotFound() {
        when(transactionRepository.findById(1)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(TransactionException.class, () -> {
            transactionService.getTransactionById(1);
        });
        
        assertEquals("Transaction not found", exception.getMessage());
        verify(transactionRepository).findById(1);
    }

    @Test
    public void testGetTransactionsByUserId_NotFound() {
        when(transactionRepository.findByUserId(1)).thenReturn(new ArrayList<>());
        
        Exception exception = assertThrows(TransactionException.class, () -> {
            transactionService.getTransactionsByUserId(1);
        }); 
        
        assertEquals("Transaction not found for the userId: 1", exception.getMessage());
        verify(transactionRepository).findByUserId(1);
    }

    @Test
    public void testMakeTransaction_InsufficientBalance() {
        TransactionRequest request = new TransactionRequest();
        request.setSenderUserId(1);
        request.setReceiverUserId(2);
        request.setAmount(BigDecimal.valueOf(300));

        when(sender.getUserId()).thenReturn(1);
        when(sender.getWallet()).thenReturn(BigDecimal.valueOf(200));
        when(userClient.getUserById(1)).thenReturn(sender);
        when(userClient.getUserById(2)).thenReturn(receiver);

        Exception exception = assertThrows(TransactionException.class, () -> {
            transactionService.makeTransaction(request);
        });

        assertEquals("Insufficient Balance", exception.getMessage());
    }

    @Test
    public void testDebtTransaction_InsufficientBalance() {
        DebtTxn debtTxn = new DebtTxn();
        debtTxn.setLoanId(1);
        debtTxn.setAmount(BigDecimal.valueOf(200));

        Debt debt = new Debt();
        debt.setUserId(1);

        when(debtService.getDebtById(1)).thenReturn(debt);
        when(userClient.getUserById(1)).thenReturn(sender);
        when(sender.getWallet()).thenReturn(BigDecimal.valueOf(100));

        Exception exception = assertThrows(TransactionException.class, () -> {
            transactionService.debtTransaction(debtTxn);
        });

        assertEquals("Insufficient Balance", exception.getMessage());
    }

    @Test
    public void testPortfolioTransaction_InsufficientBalance() {
        PortfolioDto portfolioDto = new PortfolioDto();
        portfolioDto.setUserId(1);
        portfolioDto.setPurchasePrice(BigDecimal.valueOf(200));

        when(userClient.getUserById(1)).thenReturn(sender);
        when(sender.getWallet()).thenReturn(BigDecimal.valueOf(100));

        Exception exception = assertThrows(TransactionException.class, () -> {
            transactionService.portfolioTransaction(portfolioDto);
        });

        assertEquals("Insufficient balance for user ID: 1", exception.getMessage());
    }
}
