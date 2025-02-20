package com.main.service;

import com.main.dto.DebtRequest;
import com.main.entity.Debt;
import com.main.exception.DebtException;
import com.main.exception.UserNotFoundException;
import com.main.proxy.UserClient;
import com.main.repository.DebtRepository;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
@Service
public class DebtServiceImpl implements DebtService {

    private static final Logger logger = LoggerFactory.getLogger(DebtServiceImpl.class);

    @Autowired
    private DebtRepository debtRepository;
    
    @Autowired 
    private UserClient userClient;

    @Override
    public Debt createDebt(DebtRequest debtRequest) {
    	logger.info("Creating a new Debt");
        if (userClient.getUserById(debtRequest.getUserId()) == null) {
        	logger.error("UserId not found");
            throw new UserNotFoundException("User ID not found: " + debtRequest.getUserId());
        }

        Debt debt = new Debt();
        debt.setUserId(debtRequest.getUserId());
        debt.setLoanType(debtRequest.getLoanType());
        debt.setPrincipalAmount(debtRequest.getPrincipalAmount());
        debt.setInterestRate(debtRequest.getInterestRate());
        debt.setEmiAmount(debtRequest.getEmiAmount());
        debt.setAmountPaid(debtRequest.getAmountPaid());
        debt.setStartDate(debtRequest.getStartDate());
        debt.setEndDate(debtRequest.getEndDate()); 
        return debtRepository.save(debt); 
    }

    @Override
    public Debt getDebtById(int loanId) {
    	logger.info("Fetching debt for the loanId: {}", loanId);
        return debtRepository.findById(loanId).orElseThrow(() -> { 
        	logger.error("Error Deleting debt by ID: {}", loanId); 
        	return new DebtException("Debt not found");
        });
    }

    @Override
    public Debt updateDebt(int loanId, BigDecimal amount) {
    	logger.info("Updating debt for the loanId: {}", loanId);
        Debt debt = debtRepository.findById(loanId).orElseThrow(() -> {
        	logger.error("Debt not found for the loanId: {}", loanId);
        	return new DebtException("Debt not found");
        });
        debt.setAmountPaid(debt.getAmountPaid().add(amount));
        return debtRepository.save(debt);
    }

    @Override
    public void deleteDebt(int loanId) {
        try {
        	logger.info("Deleting the debt with the loanId: {}", loanId);
            debtRepository.deleteById(loanId);
        } catch (Exception e) {
            logger.error("Error deleting debt: {}", e.getMessage());
            throw new DebtException("Failed to delete debt");
        }
    }

    @Override
    public List<Debt> getDebtsByUserId(int userId) {
        try {
        	logger.info("Fetching Debts by UserId: {}", userId);
            return debtRepository.findByUserId(userId);
        } catch (Exception e) {
            logger.error("Error fetching debts by user ID: {}", e.getMessage());
            throw new DebtException("Failed to fetch debts");
        }
    }
}
