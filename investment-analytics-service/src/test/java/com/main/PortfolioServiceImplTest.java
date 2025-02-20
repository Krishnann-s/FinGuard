package com.main;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.main.dto.PortfolioRequest;
import com.main.entity.Portfolio;
import com.main.exception.CustomException;
import com.main.proxy.FinanceClient;
import com.main.repository.PortfolioRepository;
import com.main.service.PortfolioServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

class PortfolioServiceImplTest {

    @InjectMocks
    private PortfolioServiceImpl portfolioService;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private FinanceClient financeClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPortfolio_Success() {
        PortfolioRequest request = new PortfolioRequest();
        request.setUserId(1);
        request.setAssetType("Stock");
        request.setQuantity(10);
        request.setPurchasePrice(BigDecimal.valueOf(100));
        request.setCurrentPrice(BigDecimal.valueOf(120));
        request.setPurchaseDate(LocalDate.now());

        Portfolio mockPortfolio = new Portfolio();
        mockPortfolio.setUserId(1);
        mockPortfolio.setAssetType("Stock");
        mockPortfolio.setQuantity(10);
        mockPortfolio.setPurchasePrice(BigDecimal.valueOf(100));
        mockPortfolio.setCurrentPrice(BigDecimal.valueOf(120));
        mockPortfolio.setPurchaseDate(LocalDate.now());

        when(financeClient.portfolioTransaction(request)).thenReturn(null);
        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(mockPortfolio);

        Portfolio result = portfolioService.addPortfolio(request);

        assertNotNull(result);
        assertEquals(1, result.getUserId());
        verify(financeClient).portfolioTransaction(request);
        verify(portfolioRepository).save(any(Portfolio.class));
    }

    @Test
    void testAddPortfolio_Fallback() {
        PortfolioRequest request = new PortfolioRequest();
        when(financeClient.portfolioTransaction(request)).thenThrow(new RuntimeException("Error"));

        Portfolio result = portfolioService.addPortfolioFallback(request, new RuntimeException("Error"));

        assertNull(result);
    }

    @Test
    void testViewPortfolio_Success() {
        int portfolioId = 1;
        Portfolio mockPortfolio = new Portfolio();
        mockPortfolio.setUserId(portfolioId);
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(mockPortfolio));

        Portfolio result = portfolioService.viewPortfolio(portfolioId);

        assertNotNull(result);
        assertEquals(portfolioId, result.getUserId());
    }

    @Test
    void testViewPortfolio_NotFound() {
        int portfolioId = 1;
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            portfolioService.viewPortfolio(portfolioId);
        });

        assertEquals("Portfolio not found with ID: 1", exception.getMessage());
    }

    @Test
    void testUpdatePortfolio_Success() {
        int portfolioId = 1;
        PortfolioRequest request = new PortfolioRequest();
        request.setAssetType("Updated Stock");
        request.setPurchasePrice(BigDecimal.valueOf(150));

        Portfolio mockPortfolio = new Portfolio();
        mockPortfolio.setUserId(portfolioId);
        mockPortfolio.setAssetType("Stock");
        mockPortfolio.setPurchasePrice(BigDecimal.valueOf(100));
        
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(mockPortfolio));
        when(portfolioRepository.save(mockPortfolio)).thenReturn(mockPortfolio);

        Portfolio result = portfolioService.updatePortfolio(portfolioId, request);

        assertNotNull(result);
        assertEquals("Updated Stock", result.getAssetType());
        assertEquals(BigDecimal.valueOf(150), result.getPurchasePrice());
    }

    @Test
    void testUpdatePortfolio_NotFound() {
        int portfolioId = 1;
        PortfolioRequest request = new PortfolioRequest();
        when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            portfolioService.updatePortfolio(portfolioId, request);
        });

        assertEquals("Portfolio not found with ID: 1", exception.getMessage());
    }

    @Test
    void testDeletePortfolio_Success() {
        int portfolioId = 1;
        when(portfolioRepository.existsById(portfolioId)).thenReturn(true);

        portfolioService.deletePortfolio(portfolioId);

        verify(portfolioRepository).deleteById(portfolioId);
    }

    @Test
    void testDeletePortfolio_NotFound() {
        int portfolioId = 1;
        when(portfolioRepository.existsById(portfolioId)).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> {
            portfolioService.deletePortfolio(portfolioId);
        });

        assertEquals("Portfolio not found with ID: 1", exception.getMessage());
    }

    @Test
    void testViewAllPortfolios_Success() {
        int userId = 1;
        Portfolio mockPortfolio1 = new Portfolio();
        Portfolio mockPortfolio2 = new Portfolio();
        when(portfolioRepository.findByUserId(userId)).thenReturn(Arrays.asList(mockPortfolio1, mockPortfolio2));

        List<Portfolio> result = portfolioService.viewAllPortfolios(userId);

        assertEquals(2, result.size());
    }

    @Test
    void testViewAllPortfolios_Empty() {
        int userId = 1;
        when(portfolioRepository.findByUserId(userId)).thenReturn(Arrays.asList());

        List<Portfolio> result = portfolioService.viewAllPortfolios(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testViewAllPortfolios_Fallback() {
        int userId = 1;
        when(portfolioRepository.findByUserId(userId)).thenThrow(new RuntimeException("Error"));

        List<Portfolio> result = portfolioService.viewAllPortfoliosFallback(userId, new RuntimeException("Error"));

        assertTrue(result.isEmpty());
    }
}
