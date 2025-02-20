package com.main.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.main.dto.PortfolioRequest;
import com.main.entity.Portfolio;
import com.main.exception.CustomException;
import com.main.service.PortfolioServiceImpl;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/Portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioServiceImpl portfolioService;

    
    @PostMapping
    public ResponseEntity<Portfolio> addPortfolio(@Valid @RequestBody PortfolioRequest request) {
        try {
            Portfolio portfolio = portfolioService.addPortfolio(request);
            return new ResponseEntity<>(portfolio, HttpStatus.CREATED);
        } catch (CustomException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * View a portfolio by ID.
     * 
     * @param portfolioId Portfolio ID.
     * @return Portfolio object.
     */
    @GetMapping("/{portfolioId}")
    public ResponseEntity<Portfolio> viewPortfolio(@Valid @PathVariable int portfolioId) {
        try {
            Portfolio portfolio = portfolioService.viewPortfolio(portfolioId);
            if (portfolio == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(portfolio, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update a portfolio.
     * 
     * @param portfolioId  Portfolio ID.
     * @param portfolioRequest Portfolio request object.
     * @return Updated portfolio object.
     */
    @PutMapping("/{portfolioId}")
    public ResponseEntity<Portfolio> updatePortfolio(@Valid @PathVariable int portfolioId, @RequestBody PortfolioRequest portfolioRequest) {
        try {
            Portfolio portfolio = portfolioService.updatePortfolio(portfolioId, portfolioRequest);
            if (portfolio == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(portfolio, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a portfolio.
     * 
     * @param portfolioId Portfolio ID.
     * @return HTTP status.
     */
    @DeleteMapping("/{portfolioId}")
    public ResponseEntity<Void> deletePortfolio(@Valid @PathVariable int portfolioId) {
        try {
            portfolioService.deletePortfolio(portfolioId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * View all portfolios for a user.
     * 
     * @param userId User ID.
     * @return List of portfolio objects.
     */
    @GetMapping("/portfolio/{userId}")
    public ResponseEntity<List<Portfolio>> viewAllPortfolios(@PathVariable int userId) {
        try {
            List<Portfolio> portfolios = portfolioService.viewAllPortfolios(userId);
            if (portfolios.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(portfolios, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}