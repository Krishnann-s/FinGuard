package com.main.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.main.dto.PortfolioRequest;
import com.main.entity.Portfolio;
import com.main.exception.CustomException;
import com.main.proxy.FinanceClient;
import com.main.repository.PortfolioRepository;

import jakarta.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class PortfolioServiceImpl implements PortfolioService {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioServiceImpl.class);

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private FinanceClient financeClient;

    @Override
//    @CircuitBreaker(name = "portfolioService", fallbackMethod = "addPortfolioFallback")
    public Portfolio addPortfolio(PortfolioRequest request) {
        logger.info("Adding portfolio for user ID: {}", request.getUserId());

        financeClient.portfolioTransaction(request);

        // Create portfolio record
        Portfolio portfolio = new Portfolio();
        portfolio.setUserId(request.getUserId());
        portfolio.setAssetType(request.getAssetType());
        portfolio.setQuantity(request.getQuantity());
        portfolio.setPurchasePrice(request.getPurchasePrice());
        portfolio.setCurrentPrice(request.getCurrentPrice());
        portfolio.setPurchaseDate(request.getPurchaseDate());
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        logger.info("Portfolio added successfully with ID: {}", savedPortfolio.getUserId());
        return savedPortfolio;
    }

    // Fallback method for addPortfolio
    public Portfolio addPortfolioFallback(PortfolioRequest request, Throwable throwable) {
        logger.warn("Fallback for addPortfolio due to: {}", throwable.getMessage());
        // Handle fallback logic here
        return null; // or return a default Portfolio object
    }

    @Override
//    @CircuitBreaker(name = "portfolioService", fallbackMethod = "viewPortfolioFallback")
    public Portfolio viewPortfolio(int portfolioId) {
        logger.info("Viewing portfolio with ID: {}", portfolioId);
        return portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> {
                    logger.error("Portfolio not found with ID: {}", portfolioId);
                    return new CustomException("Portfolio not found with ID: " + portfolioId);
                });
    }

    // Fallback method for viewPortfolio
    public Portfolio viewPortfolioFallback(int portfolioId, Throwable throwable) {
        logger.warn("Fallback for viewPortfolio due to: {}", throwable.getMessage());
        return null; // or handle it appropriately
    }

    @Override
//    @CircuitBreaker(name = "portfolioService", fallbackMethod = "updatePortfolioFallback")
    public Portfolio updatePortfolio(int portfolioId, PortfolioRequest portfolioRequest) {
        logger.info("Updating portfolio with ID: {}", portfolioId);

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> {
                    logger.error("Portfolio not found with ID: {}", portfolioId);
                    return new CustomException("Portfolio not found with ID: " + portfolioId);
                });

        if (portfolioRequest.getAssetType() == null) {
            logger.error("Portfolio Asset Type cannot be null for portfolio ID: {}", portfolioId);
            throw new CustomException("Portfolio Asset Type cannot be null.");
        }
        portfolio.setPurchasePrice(portfolioRequest.getPurchasePrice());

        Portfolio updatedPortfolio = portfolioRepository.save(portfolio);
        logger.info("Portfolio updated successfully with ID: {}", updatedPortfolio.getUserId());
        return updatedPortfolio;
    }

    // Fallback method for updatePortfolio
    public Portfolio updatePortfolioFallback(int portfolioId, PortfolioRequest portfolioRequest, Throwable throwable) {
        logger.warn("Fallback for updatePortfolio due to: {}", throwable.getMessage());
        return null; // or handle it appropriately
    }

    @Override
//    @CircuitBreaker(name = "portfolioService", fallbackMethod = "deletePortfolioFallback")
    public void deletePortfolio(int portfolioId) {
        logger.info("Deleting portfolio with ID: {}", portfolioId);

        if (!portfolioRepository.existsById(portfolioId)) {
            logger.error("Portfolio not found with ID: {}", portfolioId);
            throw new CustomException("Portfolio not found with ID: " + portfolioId);
        }

        portfolioRepository.deleteById(portfolioId);
        logger.info("Portfolio deleted successfully with ID: {}", portfolioId);
    }

    // Fallback method for deletePortfolio
    public void deletePortfolioFallback(int portfolioId, Throwable throwable) {
        logger.warn("Fallback for deletePortfolio due to: {}", throwable.getMessage());
        // Handle fallback logic here, e.g., log the error or notify the user
    }

    @Override
//    @CircuitBreaker(name = "portfolioService", fallbackMethod = "viewAllPortfoliosFallback")
    public List<Portfolio> viewAllPortfolios(int userId) {
        logger.info("Retrieving all portfolios for user ID: {}", userId);
        List<Portfolio> portfolios = portfolioRepository.findByUserId(userId);
        
        if (portfolios.isEmpty()) {
            logger.warn("No portfolios found for user ID: {}", userId); 
        } else {
            logger.info("Retrieved {} portfolios for user ID: {}", portfolios.size(), userId);
        }
        
        return portfolios;
    }

    // Fallback method for viewAllPortfolios
    public List<Portfolio> viewAllPortfoliosFallback(int userId, Throwable throwable) {
        logger.warn("Fallback for viewAllPortfolios due to: {}", throwable.getMessage());
        // Handle fallback logic here, e.g., return an empty list or a default response
        return List.of(); // Return an empty list as a fallback
    }
    }