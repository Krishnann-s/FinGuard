//package com.main;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import java.util.Optional;
//import java.util.List;
//import java.math.BigDecimal;
//import java.util.Arrays;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import com.main.dto.BudgetDto;
//import com.main.entity.Budget;
//import com.main.exception.BudgetNotFoundException;
//import com.main.exception.InvalidBudgetException;
//import com.main.exception.RemainingAmountException;
//import com.main.proxy.UserClient;
//import com.main.repository.BudgetRepository;
//import com.main.service.BudgetServiceImpl;
//
//@SpringBootTest
//class BudgetServiceImplTest {
//
//	    @Mock
//	    private BudgetRepository budgetRepository;
//
//	    @Mock
//	    private UserClient userClient;
//
//	    @InjectMocks
//	    private BudgetServiceImpl budgetService;
//
//	    @BeforeEach
//	    public void setUp() {
//	        MockitoAnnotations.initMocks(this);
//	    }
//
//	    @Test
//	    public void testCreateBudgetService_Success() {
//	        BudgetDto budgetDto = new BudgetDto();
//	        budgetDto.setUserId(1);
//	        budgetDto.setAmount(BigDecimal.valueOf(1000));
//	        budgetDto.setSpentAmount(BigDecimal.valueOf(200));
//	        budgetDto.setCategory("Food");
//
//	        Budget budget = new Budget();
//	        budget.setUserId(1);
//	        budgetDto.setAmount(BigDecimal.valueOf(1000));
//	        budgetDto.setSpentAmount(BigDecimal.valueOf(200));
//	        budget.setCategory("Food");
//
//	        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);
//
//	        Budget createdBudget = budgetService.createBudgetService(budgetDto);
//	        assertNotNull(createdBudget);
//	        assertEquals(1, createdBudget.getUserId());
//	        assertEquals(1000, createdBudget.getAmount());
//	        assertEquals("Food", createdBudget.getCategory());
//
//	        verify(budgetRepository, times(1)).save(any(Budget.class));
//	    }
//
//	    @Test
//	    public void testCreateBudgetService_InvalidBudgetAmount() {
//	        BudgetDto budgetDto = new BudgetDto();
//	        budgetDto.setUserId(1);
//	        budgetDto.setAmount(BigDecimal.valueOf(1000));
//	        budgetDto.setSpentAmount(BigDecimal.valueOf(200));
//
//	        assertThrows(InvalidBudgetException.class, () -> {
//	            budgetService.createBudgetService(budgetDto);
//	        });
//
//	        verify(budgetRepository, never()).save(any(Budget.class));
//	    }
//
//	    @Test
//	    public void testGetBudgetByIdService_Success() {
//	        Budget budget = new Budget();
//	        budget.setUserId(1);
//	        budget.setAmount(BigDecimal.valueOf(1000));
//	        budget.setSpentAmount(BigDecimal.valueOf(200));
//
//	        when(budgetRepository.findById(1)).thenReturn(Optional.of(budget));
//
//	        Budget fetchedBudget = budgetService.getBudgetByIdService(1);
//	        assertNotNull(fetchedBudget);
//	        assertEquals(1, fetchedBudget.getUserId());
//
//	        verify(budgetRepository, times(1)).findById(1);
//	    }
//
//	    @Test
//	    public void testGetBudgetByIdService_BudgetNotFound() {
//	        when(budgetRepository.findById(1)).thenReturn(Optional.empty());
//
//	        assertThrows(BudgetNotFoundException.class, () -> {
//	            budgetService.getBudgetByIdService(1);
//	        });
//
//	        verify(budgetRepository, times(1)).findById(1);
//	    }
//
//	    @Test
//	    public void testUpdateBudgetService_Success() {
//	        Budget budget = new Budget();
//	        budget.setUserId(1);
//	        budget.setAmount(BigDecimal.valueOf(1000));
//
//	        BudgetDto budgetDto = new BudgetDto();
//	        budgetDto.setAmount(BigDecimal.valueOf(2000));
//	        budgetDto.setCategory("Entertainment");
//
//	        when(budgetRepository.findById(1)).thenReturn(Optional.of(budget));
//	        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);
//
//	        Budget updatedBudget = budgetService.updateBudgetService(1, budgetDto);
//	        assertNotNull(updatedBudget);
//	        assertEquals(2000, updatedBudget.getAmount());
//	        assertEquals("Entertainment", updatedBudget.getCategory());
//
//	        verify(budgetRepository, times(1)).findById(1);
//	        verify(budgetRepository, times(1)).save(any(Budget.class));
//	    }
//	    
//	    @Test
//	    public void testDeleteBudgetService_Success() {
//	        Budget budget = new Budget();
//	        budget.setUserId(1);
//
//	        when(budgetRepository.findById(1)).thenReturn(Optional.of(budget));
//
//	        budgetService.deleteBudgetService(1);
//
//	        verify(budgetRepository, times(1)).delete(budget);
//	    }
//
//	    @Test
//	    public void testGetUserBudgetsService() {
//	        Budget budget1 = new Budget();
//	        budget1.setUserId(1);
//
//	        when(budgetRepository.findByUserId(1)).thenReturn(budget1);
//
//	        Budget budget = budgetService.getUserBudgetsService(1);
//	        assertNotNull(budget);
//	        assertEquals(1, budget.getUserId());
//
//	        verify(budgetRepository, times(1)).findByUserId(1);
//	    }
//
//	    @Test
//	    public void testGetRemainingAmountService_Success() {
//	        Budget budget = new Budget();
//	        budget.setUserId(1);
//	        budget.setAmount(BigDecimal.valueOf(1000));
//	        budget.setSpentAmount(BigDecimal.valueOf(500));
//
//	        when(budgetRepository.findById(1)).thenReturn(Optional.of(budget));
//
//	        BigDecimal remainingAmount = budgetService.getRemainingAmountService(1);
//	        assertEquals(500, remainingAmount);
//
//	        verify(budgetRepository, times(1)).findById(1);
//	    }
//
//	    @Test
//	    public void testGetRemainingAmountService_SpentExceedsBudget() {
//	        Budget budget = new Budget();
//	        budget.setUserId(1);
//	        budget.setAmount(BigDecimal.valueOf(1000));
//	        budget.setSpentAmount(BigDecimal.valueOf(1200)); // Spent exceeds budget
//
//	        when(budgetRepository.findById(1)).thenReturn(Optional.of(budget));
//
//	        assertThrows(RemainingAmountException.class, () -> {
//	            budgetService.getRemainingAmountService(1);
//	        });
//
//	        verify(budgetRepository, times(1)).findById(1);
//	    }
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////package com.main;
////
////import static org.junit.jupiter.api.Assertions.*;
////import static org.mockito.Mockito.*;
////
////import java.math.BigDecimal;
////import java.time.LocalDate;
////import java.util.Arrays;
////import java.util.Collections;
////import java.util.List;
////import java.util.Optional;
////
////import org.junit.jupiter.api.BeforeEach;
////import org.junit.jupiter.api.Test;
////import org.junit.jupiter.api.extension.ExtendWith;
////import org.mockito.InjectMocks;
////import org.mockito.Mock;
////import org.mockito.junit.jupiter.MockitoExtension;
////
////import com.main.dto.BudgetDto;
////import com.main.dto.BudgetReportRequest;
////import com.main.dto.BudgetResponse;
////import com.main.entity.Budget;
////import com.main.entity.Transaction;
////import com.main.exception.BudgetNotFoundException;
////import com.main.exception.InvalidBudgetException;
////import com.main.exception.RemainingAmountException;
////import com.main.repository.BudgetRepository;
////import com.main.service.BudgetServiceImpl;
////import com.main.service.TransactionService;
////
////@ExtendWith(MockitoExtension.class)
////class BudgetServiceImplTest {
////
////    @Mock
////    private BudgetRepository budgetRepository;
////
////    @Mock
////    private TransactionService transactionService;
////
////    @InjectMocks
////    private BudgetServiceImpl budgetService;
////
////    private BudgetDto validBudgetDto;
////    private Budget budget;
////
////    @BeforeEach
////    void setup() {
////        validBudgetDto = new BudgetDto(1, new BigDecimal("1000"), new BigDecimal("0"), "Food", LocalDate.now(), LocalDate.now().plusMonths(1));
////        budget = new Budget(1, 1, "Food", new BigDecimal("1000"), new BigDecimal("0"), LocalDate.now(), LocalDate.now().plusMonths(1),LocalDate.now(),LocalDate.now());
////    }
////
////    // Positive Test Cases
////
////    @Test
////    void testCreateBudgetService_Success() {
////        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);
////
////        Budget createdBudget = budgetService.createBudgetService(validBudgetDto);
////
////        assertNotNull(createdBudget);
////        assertEquals(validBudgetDto.getUserId(), createdBudget.getUserId());
////        assertEquals(validBudgetDto.getAmount(), createdBudget.getAmount());
////        assertEquals(validBudgetDto.getCategory(), createdBudget.getCategory());
////    }
////
////    @Test
////    void testGetBudgetByIdService_Success() {
////        when(budgetRepository.findById(1)).thenReturn(Optional.of(budget));
////
////        Budget foundBudget = budgetService.getBudgetByIdService(1);
////
////        assertNotNull(foundBudget);
////        assertEquals(1, foundBudget.getBudgetId());
////        assertEquals(budget.getAmount(), foundBudget.getAmount());
////    }
////
////    @Test
////    void testUpdateBudgetService_Success() {
////        BudgetDto updateBudgetDto = new BudgetDto(1, new BigDecimal("2000"), new BigDecimal("500"), "Travel", LocalDate.now(), LocalDate.now().plusMonths(1));
////
////        when(budgetRepository.findById(1)).thenReturn(Optional.of(budget));
////        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);
////
////        Budget updatedBudget = budgetService.updateBudgetService(1, updateBudgetDto);
////
////        assertNotNull(updatedBudget);
////        assertEquals(updateBudgetDto.getAmount(), updatedBudget.getAmount());
////        assertEquals(updateBudgetDto.getCategory(), updatedBudget.getCategory());
////    }
////
////    @Test
////    void testDeleteBudgetService_Success() {
////        when(budgetRepository.findById(1)).thenReturn(Optional.of(budget));
////        doNothing().when(budgetRepository).delete(budget);
////
////        assertDoesNotThrow(() -> budgetService.deleteBudgetService(1));
////
////        verify(budgetRepository, times(1)).delete(budget);
////    }
////
////    @Test
////    void testGetRemainingAmountService_Success() {
////        when(budgetRepository.findById(1)).thenReturn(Optional.of(budget));
////
////        BigDecimal remainingAmount = budgetService.getRemainingAmountService(1);
////
////        assertEquals(new BigDecimal("1000"), remainingAmount);
////    }
////
////    @Test
////    public void testGetBudgetReport_ValidRequest() {
////        // Arrange
////        BudgetReportRequest request = new BudgetReportRequest();
////        request.setUserId(1);
////        
////        List<Transaction> transactions = Arrays.asList(
////            new Transaction(1, 1, BigDecimal.valueOf(100), BigDecimal.valueOf(500), "Debited", LocalDate.now(), LocalDate.now(), LocalDate.now()),
////            new Transaction(2, 1, BigDecimal.valueOf(50), BigDecimal.valueOf(400), "Debited", LocalDate.now(), LocalDate.now(), LocalDate.now())
////        );
////        
////        Budget budget = new Budget();
////        budget.setBudgetId(1);
////        budget.setUserId(1);
////        budget.setCategory("Groceries");
////        budget.setAmount(BigDecimal.valueOf(1000));
////        budget.setSpentAmount(BigDecimal.valueOf(150));
////        budget.setStartDate(LocalDate.of(2024, 10, 1));
////        budget.setEndDate(LocalDate.of(2024, 10, 31));
////        budget.setCreatedAt(LocalDate.now());
////        budget.setUpdatedAt(LocalDate.now());
////        
////        when(transactionService.monthlyExpense(request)).thenReturn(transactions);
////        when(budgetRepository.findByUserId(1)).thenReturn(budget);
////        
////        // Act
////        BudgetResponse response = budgetService.getBudgetReport(request);
////        
////        // Assert
////        assertEquals(BigDecimal.valueOf(150), response.getExpense());
////        assertEquals(BigDecimal.valueOf(1000), response.getAmount());
////        assertEquals("Groceries", budget.getCategory());
////    }
////
////
////    // Negative Test Cases
////
////    @Test
////    void testCreateBudgetService_InvalidAmount() {
////        BudgetDto invalidBudgetDto = new BudgetDto(1, new BigDecimal("-1000"), new BigDecimal("0"), "Food", LocalDate.now(), LocalDate.now().plusMonths(1));
////
////        assertThrows(InvalidBudgetException.class, () -> budgetService.createBudgetService(invalidBudgetDto));
////    }
////
////    @Test
////    void testGetBudgetByIdService_NotFound() {
////        when(budgetRepository.findById(1)).thenReturn(Optional.empty());
////
////        assertThrows(BudgetNotFoundException.class, () -> budgetService.getBudgetByIdService(1));
////    }
////
////    @Test
////    void testUpdateBudgetService_InvalidAmount() {
////        BudgetDto invalidUpdateBudgetDto = new BudgetDto(1, new BigDecimal("-1000"), new BigDecimal("500"), "Travel", LocalDate.now(), LocalDate.now().plusMonths(1));
////
////        when(budgetRepository.findById(1)).thenReturn(Optional.of(budget));
////
////        assertThrows(InvalidBudgetException.class, () -> budgetService.updateBudgetService(1, invalidUpdateBudgetDto));
////    }
////
////    @Test
////    void testGetRemainingAmountService_ExceededSpentAmount() {
////        Budget overSpentBudget = new Budget(1, 1, "Food", new BigDecimal("1000"), new BigDecimal("1200"), LocalDate.now(), LocalDate.now().plusMonths(1),LocalDate.now(),LocalDate.now());
////
////        when(budgetRepository.findById(1)).thenReturn(Optional.of(overSpentBudget));
////
////        assertThrows(RemainingAmountException.class, () -> budgetService.getRemainingAmountService(1));
////    }
////
////    @Test
////    void testDeleteBudgetService_NotFound() {
////        when(budgetRepository.findById(1)).thenReturn(Optional.empty());
////
////        assertThrows(BudgetNotFoundException.class, () -> budgetService.deleteBudgetService(1));
////    }
////
////    @Test
////    void testGetUserBudgetsService_Success() {
////        when(budgetRepository.findByUserId(1)).thenReturn(budget);
////
////        Budget userBudgets = budgetService.getUserBudgetsService(1);
////
////        assertNotNull(userBudgets);
////        assertEquals(budget.getAmount(), userBudgets.getAmount());
////    }
////
////    @Test
////    void testGetUserBudgetsService_NotFound() {
////        when(budgetRepository.findByUserId(1)).thenReturn(null);
////
////        assertThrows(BudgetNotFoundException.class, () -> budgetService.getUserBudgetsService(1));
////    }
////
////    @Test
////    void testGetBudgetReport_NoTransactions() {
////        BudgetReportRequest reportRequest = new BudgetReportRequest(1, LocalDate.now().minusMonths(1), LocalDate.now());
////
////        when(budgetRepository.findByUserId(1)).thenReturn(budget);
////        when(transactionService.monthlyExpense(reportRequest)).thenReturn(Collections.emptyList());
////
////        BudgetResponse budgetResponse = budgetService.getBudgetReport(reportRequest);
////
////        assertNotNull(budgetResponse);
////        assertEquals(BigDecimal.ZERO, budgetResponse.getExpense());
////        assertEquals(budget.getAmount(), budgetResponse.getAmount());
////    }
////    @Test
////    void testGetBudgetReport_Success() {
////        // Given: A valid BudgetReportRequest for a user with a budget and transactions in the previous month
////        BudgetReportRequest reportRequest = new BudgetReportRequest(1, LocalDate.now().minusMonths(1), LocalDate.now());
////
////        // Sample Transactions for the budget report
////        Transaction transaction1 = new Transaction(1, 1, new BigDecimal("300"), BigDecimal.ZERO, "Debited", LocalDate.now(), LocalDate.now(), LocalDate.now());
////        Transaction transaction2 = new Transaction(2, 1, new BigDecimal("100"), BigDecimal.ZERO, "Debited", LocalDate.now(), LocalDate.now(), LocalDate.now());
////
////        // Sample Budget for the User
////        Budget budget = new Budget(
////                1, 
////                1, 
////                "Food", 
////                new BigDecimal("1000"), 
////                new BigDecimal("400"), 
////                LocalDate.now(), 
////                LocalDate.now().plusMonths(1),
////                LocalDate.now(),
////                LocalDate.now()
////        );
////
////        // Mock the behavior of the transactionService and budgetRepository
////        when(budgetRepository.findByUserId(1)).thenReturn(budget);
////        when(transactionService.monthlyExpense(reportRequest)).thenReturn(List.of(transaction1, transaction2));
////
////        // When: Call the method to get the budget report
////        BudgetResponse budgetResponse = budgetService.getBudgetReport(reportRequest);
////
////        // Then: Verify the budget report response
////        assertNotNull(budgetResponse);
////        assertEquals(new BigDecimal("400"), budgetResponse.getExpense());  // Total expenses from transactions
////        assertEquals(budget.getAmount(), budgetResponse.getAmount());      // Total budget amount
////    }
////
////}
