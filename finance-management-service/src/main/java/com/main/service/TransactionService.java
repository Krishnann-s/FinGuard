package com.main.service;

import java.math.BigDecimal;
import java.util.List;

import com.main.dto.BudgetReportRequest;
import com.main.dto.DebtRequest;
import com.main.dto.DebtTxn;
import com.main.dto.PortfolioDto;
import com.main.dto.TransactionRequest;
import com.main.entity.Transaction;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction);
    Transaction getTransactionById(int id);
//    Transaction updateTransaction(int id, Transaction transaction);
//    Boolean deleteTransaction(int id);
    List<Transaction> getTransactionsByUserId(int userId);
//    void makeTransaction(int senderUserId, int receiverUserId, double amount);
    String makeTransaction(TransactionRequest txn);
    void debtTransaction(DebtTxn debtTxn);
    void portfolioTransaction(PortfolioDto portfolioDto);
    List<Transaction> monthlyExpense(BudgetReportRequest budgetReportRequest);
}

