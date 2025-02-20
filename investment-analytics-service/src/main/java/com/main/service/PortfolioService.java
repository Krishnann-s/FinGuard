package com.main.service;

import java.util.List;
import com.main.dto.PortfolioRequest;
import com.main.entity.Portfolio;

public interface PortfolioService {
    Portfolio addPortfolio(PortfolioRequest request);
    Portfolio viewPortfolio(int portfolioId);
    Portfolio updatePortfolio(int portfolioId, PortfolioRequest portfolioRequest);
    void deletePortfolio(int portfolioId);
	List<Portfolio> viewAllPortfolios(int userId);
}