package com.main;

import com.main.controller.PortfolioController;
import com.main.dto.PortfolioRequest;

import com.main.entity.Portfolio;
import com.main.exception.CustomException;
import com.main.service.PortfolioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class PortfolioControllerTest {

    @InjectMocks
    private PortfolioController portfolioController;

    @Mock
    private PortfolioServiceImpl portfolioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddPortfolio_Success() {
        PortfolioRequest request = new PortfolioRequest();
        Portfolio portfolio = new Portfolio();
        when(portfolioService.addPortfolio(any(PortfolioRequest.class))).thenReturn(portfolio);

        ResponseEntity<Portfolio> response = portfolioController.addPortfolio(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(portfolio, response.getBody());
    }

    @Test
    public void testAddPortfolio_CustomException() {
        PortfolioRequest request = new PortfolioRequest();
        when(portfolioService.addPortfolio(any(PortfolioRequest.class))).thenThrow(new CustomException("Bad Request"));

        ResponseEntity<Portfolio> response = portfolioController.addPortfolio(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testViewPortfolio_Success() {
        int portfolioId = 1;
        Portfolio portfolio = new Portfolio();
        when(portfolioService.viewPortfolio(portfolioId)).thenReturn(portfolio);

        ResponseEntity<Portfolio> response = portfolioController.viewPortfolio(portfolioId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(portfolio, response.getBody());
    }

    @Test
    public void testViewPortfolio_NotFound() {
        int portfolioId = 1;
        when(portfolioService.viewPortfolio(portfolioId)).thenReturn(null);

        ResponseEntity<Portfolio> response = portfolioController.viewPortfolio(portfolioId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testViewPortfolio_CustomException() {
        int portfolioId = 1;
        when(portfolioService.viewPortfolio(portfolioId)).thenThrow(new CustomException("Bad Request"));

        ResponseEntity<Portfolio> response = portfolioController.viewPortfolio(portfolioId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdatePortfolio_Success() {
        int portfolioId = 1;
        PortfolioRequest request = new PortfolioRequest();
        Portfolio portfolio = new Portfolio();
        when(portfolioService.updatePortfolio(eq(portfolioId), any(PortfolioRequest.class))).thenReturn(portfolio);

        ResponseEntity<Portfolio> response = portfolioController.updatePortfolio(portfolioId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(portfolio, response.getBody());
    }

    @Test
    public void testUpdatePortfolio_NotFound() {
        int portfolioId = 1;
        PortfolioRequest request = new PortfolioRequest();
        when(portfolioService.updatePortfolio(eq(portfolioId), any(PortfolioRequest.class))).thenReturn(null);

        ResponseEntity<Portfolio> response = portfolioController.updatePortfolio(portfolioId, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdatePortfolio_CustomException() {
        int portfolioId = 1;
        PortfolioRequest request = new PortfolioRequest();
        when(portfolioService.updatePortfolio(eq(portfolioId), any(PortfolioRequest.class))).thenThrow(new CustomException("Bad Request"));

        ResponseEntity<Portfolio> response = portfolioController.updatePortfolio(portfolioId, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeletePortfolio_Success() {
        int portfolioId = 1;
        doNothing().when(portfolioService).deletePortfolio(portfolioId);

        ResponseEntity<Void> response = portfolioController.deletePortfolio(portfolioId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeletePortfolio_CustomException() {
        int portfolioId = 1;
        doThrow(new CustomException("Bad Request")).when(portfolioService).deletePortfolio(portfolioId);

        ResponseEntity<Void> response = portfolioController.deletePortfolio(portfolioId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
        