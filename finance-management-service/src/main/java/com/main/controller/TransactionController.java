package com.main.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.main.dto.DebtRequest;
import com.main.dto.DebtTxn;
import com.main.dto.PortfolioDto;
import com.main.dto.TransactionRequest;
import com.main.entity.Transaction;
import com.main.exception.TransactionException;
import com.main.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

	private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService; 

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody @Valid Transaction transaction) {
        logger.info("Creating a new transaction: {}", transaction);
        if (transaction == null) {
            logger.error("Invalid transaction: transaction is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Invalid transaction
        }
        try {
            Transaction createdTransaction = transactionService.createTransaction(transaction);
            logger.info("Transaction created successfully with ID: {}", createdTransaction.getTxnId());
            return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating transaction: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}/{userName}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable int id, @PathVariable String userName) {
        logger.info("Fetching transaction with ID: {} for user: {}", id, userName);
        try {
            Transaction transaction = transactionService.getTransactionById(id);
            if (transaction == null) {
                logger.error("Transaction not found with ID: {}", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Transaction not found
            }
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } catch (TransactionException e) {
            logger.error("Transaction not found: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error fetching transaction: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionsByUserId(@PathVariable int userId) {
        logger.info("Fetching transactions for user ID: {}", userId);
        try {
            List<Transaction> transactions = transactionService.getTransactionsByUserId(userId);
            if (transactions == null || transactions.isEmpty()) {
                logger.warn("No transactions found for user ID: {}", userId);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No transactions found
            }
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error fetching transactions for user ID: {}: {}", userId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/user/txn")
    public ResponseEntity<String> makeTransaction(@RequestBody @Valid TransactionRequest txn) {
        logger.info("Making a transaction with request: {}", txn);
        if (txn == null) {
            logger.error("Invalid transaction request: txn is null");
            return new ResponseEntity<>("Invalid transaction request", HttpStatus.BAD_REQUEST); // Invalid request
        }
        try {
            String msg = transactionService.makeTransaction(txn);
            logger.info("Transaction processed successfully: {}", msg);
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error processing transaction: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/user/portfolio")
    public ResponseEntity<Void> portfolioTransaction(@RequestBody @Valid PortfolioDto portfolioDto) {
        logger.info("Processing portfolio transaction with details: {}", portfolioDto);
        if (portfolioDto == null || portfolioDto.getAssetType().isEmpty()) {
            logger.error("Invalid portfolio data: portfolioDto is null or assetType is empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Invalid portfolio data
        }
        try {
            transactionService.portfolioTransaction(portfolioDto);
            logger.info("Portfolio transaction processed successfully");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error processing portfolio transaction: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
        }
    }
    
    @PostMapping("/user/debt")
    public ResponseEntity<Void> debtTransaction(@RequestBody @Valid DebtTxn debtTxn) {
        logger.info("Processing debt transaction with details: {}", debtTxn);
        if (debtTxn == null) {
            logger.error("Invalid debt data: debtTxn is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Invalid debt data
        }
        try {
            transactionService.debtTransaction(debtTxn);
            logger.info("Debt transaction processed successfully");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error processing debt transaction: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
