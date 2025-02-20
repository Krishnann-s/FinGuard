package com.main.controller;

import com.main.dto.DebtRequest;
import com.main.entity.Debt;
import com.main.exception.DebtException;
import com.main.service.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/debts") 
public class DebtController {
	
	private static final Logger logger = LoggerFactory.getLogger(DebtController.class);

    @Autowired
    private DebtService debtService;

    @PostMapping
    public ResponseEntity<Debt> createDebt(@RequestBody DebtRequest debtRequest) {
        logger.info("Creating a new debt with details: {}", debtRequest);
        try {
            Debt createdDebt = debtService.createDebt(debtRequest);
            logger.info("Debt created successfully with ID: {}", createdDebt.getLoanId());
            return new ResponseEntity<>(createdDebt, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating debt: {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<Debt> getDebtById(@PathVariable Integer loanId) {
        logger.info("Fetching debt with ID: {}", loanId);
        try {
            Debt debt = debtService.getDebtById(loanId);
            logger.info("Debt retrieved successfully: {}", debt);
            return new ResponseEntity<>(debt, HttpStatus.OK);
        } catch (DebtException e) {
            logger.error("Debt not found with ID: {}", loanId);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{loanId}")
    public ResponseEntity<Void> deleteDebt(@PathVariable Integer loanId) {
        logger.info("Deleting debt with ID: {}", loanId);
        try {
            debtService.deleteDebt(loanId);
            logger.info("Debt with ID: {} deleted successfully", loanId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DebtException e) {
            logger.error("Attempted to delete non-existent debt with ID: {}", loanId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Debt>> getDebtsByUserId(@PathVariable Integer userId) {
        logger.info("Fetching debts for user ID: {}", userId);
        List<Debt> debts = debtService.getDebtsByUserId(userId);
        if (debts.isEmpty()) {
            logger.warn("No debts found for user ID: {}", userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.info("Retrieved {} debts for user ID: {}", debts.size(), userId);
        return new ResponseEntity<>(debts, HttpStatus.OK);
    }
}
