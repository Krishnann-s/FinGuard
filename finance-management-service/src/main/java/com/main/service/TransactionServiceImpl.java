package com.main.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.dto.BudgetReportRequest;
import com.main.dto.DebtRequest;
import com.main.dto.DebtTxn;
import com.main.dto.EmailResponse;
import com.main.dto.PortfolioDto;
import com.main.dto.TransactionRequest;
import com.main.dto.User;
import com.main.entity.Debt;
import com.main.entity.Transaction;
import com.main.exception.DebtException;
import com.main.exception.TransactionException;
import com.main.exception.UserNotFoundException;
import com.main.proxy.NotificationClient;
import com.main.proxy.UserClient;
import com.main.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserClient userClient; 
    
    @Autowired
    private DebtService debtService;
    
    @Autowired 
    private NotificationClient notificationClient;
    
    @Override
	public Transaction createTransaction(Transaction transaction) {
		return transactionRepository.save(transaction);
	}

    @Override 
    public Transaction getTransactionById(int id) {
        logger.info("Fetching transaction with ID: {}", id);
        return transactionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Transaction with ID {} not found", id);
                    return new TransactionException("Transaction not found");
                });
    } 

    @Override
    public List<Transaction> getTransactionsByUserId(int userId) {
        logger.info("Fetching transactions for user ID: {}", userId);
        List<Transaction> transactions = transactionRepository.findByUserId(userId); 
        if (transactions == null || transactions.isEmpty()) {
            logger.warn("No transactions found for user ID: {}", userId);
            throw new TransactionException("Transaction not found for the userId: " + userId);
        }
        return transactions;
    }
    
    @Override
    public String makeTransaction(TransactionRequest txn) {
        logger.info("Making transaction from user ID: {} to user ID: {} with amount: {}", txn.getSenderUserId(), txn.getReceiverUserId(), txn.getAmount());
        
        User receiver = userClient.getUserById(txn.getReceiverUserId());
        if (receiver == null) {
            logger.error("Receiver user with ID {} not found", txn.getReceiverUserId());
            throw new UserNotFoundException("Receiver user not found");
        }
        
        User sender = userClient.getUserById(txn.getSenderUserId());
        if (sender == null) { 
            logger.error("Sender user with ID {} not found", txn.getSenderUserId());
            throw new UserNotFoundException("Sender user not found");
        } 
        
//        BigDecimal amountBigDecimal = BigDecimal.valueOf(txn.getAmount());
        
        if (sender.getWallet().compareTo(txn.getAmount()) < 0) {
            logger.error("Insufficient balance for user ID {}", txn.getSenderUserId());
            throw new TransactionException("Insufficient Balance");
        } 
        
        receiver.setWallet(receiver.getWallet().add(txn.getAmount()));
        sender.setWallet(sender.getWallet().subtract(txn.getAmount()));
        
        userClient.updateUser(txn.getReceiverUserId(), receiver);
        Transaction receiverTxn = new Transaction();
        receiverTxn.setUserId(txn.getReceiverUserId());
        receiverTxn.setAmount(txn.getAmount());
        receiverTxn.setTxnType("Credited");
        receiverTxn.setWallet(receiver.getWallet());
        logger.info("receiver txn: {}", receiverTxn); 
        transactionRepository.save(receiverTxn);
        
        userClient.updateUser(txn.getSenderUserId(), sender);
        Transaction senderTxn = new Transaction();
        senderTxn.setUserId(txn.getSenderUserId());
        senderTxn.setAmount(txn.getAmount());
        senderTxn.setTxnType("Debited");
        senderTxn.setWallet(sender.getWallet());
        logger.info("sender txn: {}", senderTxn);
        transactionRepository.save(senderTxn);
        
        EmailResponse emailResponseSender = new EmailResponse();
        emailResponseSender.setToEmail(sender.getEmail());
        emailResponseSender.setSubject("Transaction Successful!");
        emailResponseSender.setMessage(
        	    String.format(
        	        "Dear %s, %n%nYou have successfully transferred an amount of $%.2f to %s. "
        	        + "Your updated account balance is $%.2f.%n%nThank you for using FinGuard "
        	        + "for your financial management.%n%nBest regards, %nThe FinGuard Team",
        	        sender.getUserName(),
        	        txn.getAmount(),
        	        receiver.getUserName(),
        	        sender.getWallet()
        	    )
        	);        
        
//        notificationClient.sendWalletUpdateEmail(emailResponseSender);
        
        EmailResponse emailResponseReceiver = new EmailResponse();
        emailResponseReceiver.setToEmail(receiver.getEmail());
        emailResponseReceiver.setSubject("Transaction Successful!");
        emailResponseReceiver.setMessage(
        	    String.format(
        	        "Dear %s, %n%nYou have successfully received an amount of $%.2f from %s. "
        	        + "Your updated account balance is $%.2f.%n%nThank you for using FinGuard for your "
        	        + "financial management.%n%nBest regards, %nThe FinGuard Team",
        	        receiver.getUserName(),
        	        txn.getAmount(),
        	        sender.getUserName(),
        	        receiver.getWallet()
        	    )
        	);        
        
//        notificationClient.sendWalletUpdateEmail(emailResponseReceiver);
        
        logger.info("Transaction completed successfully from user ID: {} to user ID: {} with amount: {}", txn.getSenderUserId(), txn.getReceiverUserId(), txn.getAmount());
        return "Transaction successful!!";
    }

    @Override
    public void debtTransaction(DebtTxn debtTxn) {
        logger.info("Making Transaction to the Debt Service");
        
        Debt debt = debtService.getDebtById(debtTxn.getLoanId());
        if (debt == null) {
            logger.error("Debt not found for Loan ID: {}", debtTxn.getLoanId());
            throw new DebtException("Debt not found");
        }

        User user = userClient.getUserById(debt.getUserId());
        if (user == null) {
            logger.error("User not found for User ID: {}", debt.getUserId());
            throw new UserNotFoundException("User not found");
        }

        if (user.getWallet().compareTo(debtTxn.getAmount()) < 0) {
            logger.error("Insufficient balance for user ID {}", debt.getUserId());
            throw new TransactionException("Insufficient Balance");
        }

        debtService.updateDebt(debtTxn.getLoanId(), debtTxn.getAmount());
        user.setWallet(user.getWallet().subtract(debtTxn.getAmount()));
        userClient.updateUser(user.getUserId(), user);
        
        Transaction txn = new Transaction();
        txn.setUserId(debt.getUserId());
        txn.setAmount(debtTxn.getAmount());
        txn.setTxnType("Debited");
        txn.setWallet(user.getWallet());
        transactionRepository.save(txn);
        
        EmailResponse emailResponseSender = new EmailResponse();
        emailResponseSender.setToEmail(user.getEmail());
        emailResponseSender.setSubject("Debt Transaction Successful!");
        emailResponseSender.setMessage(     String.format(       
        		"Dear %s, %n%nAn amount of $%.2f has been debited from your account for loan ID: %d. "
        		+ "Your updated account balance is $%.2f.%n%nPlease ensure to keep track of your loan "
        		+ "payments to avoid any penalties. %n%nThank you for choosing FinGuard for your financial needs.%n%nBest regards, %nThe FinGuard Team"
        		, user.getUserName(), txn.getAmount(), debtTxn.getLoanId(), user.getWallet() ) );        
//        notificationClient.sendWalletUpdateEmail(emailResponseSender);
        
        logger.info("Debt transaction completed successfully for Loan ID: {}", debtTxn.getLoanId());
    }
    
    @Override
    public void portfolioTransaction(PortfolioDto portfolioDto) {
        logger.info("Starting portfolio transaction for user ID: {}", portfolioDto.getUserId());

        // Fetching user details
        User user = userClient.getUserById(portfolioDto.getUserId());
        if (user == null) {
            logger.error("User with ID {} not found", portfolioDto.getUserId());
            throw new UserNotFoundException("User ID not found: " + portfolioDto.getUserId());
        }

        // Checking if the user has sufficient balance
        if (user.getWallet().compareTo(portfolioDto.getPurchasePrice()) < 0) {
            logger.error("Insufficient balance for user ID {}. Wallet balance: {}, Purchase price: {}", 
                         portfolioDto.getUserId(), user.getWallet(), portfolioDto.getPurchasePrice());
            throw new TransactionException("Insufficient balance for user ID: " + portfolioDto.getUserId());
        }

        // Updating the user wallet (wallet - purchasePrice) 
        user.setWallet(user.getWallet().subtract(portfolioDto.getPurchasePrice()));
        userClient.updateUser(portfolioDto.getUserId(), user);
        logger.info("Updated wallet for user ID {}. New wallet balance: {}", portfolioDto.getUserId(), user.getWallet());

        // Creating transaction record
        Transaction txnDto = new Transaction(); 
        txnDto.setUserId(portfolioDto.getUserId());
        txnDto.setAmount(portfolioDto.getPurchasePrice());
        txnDto.setWallet(user.getWallet());
        txnDto.setTxnType("Debit");
        transactionRepository.save(txnDto);

        EmailResponse emailResponseSender = new EmailResponse();
        emailResponseSender.setToEmail(user.getEmail());
        emailResponseSender.setSubject("Investment Transaction Successful!");
        emailResponseSender.setMessage(
        	    String.format(
        	        "Dear %s, %n%nAn amount of $%.2f has been successfully debited from your account for your mutual fund investment. "
        	        + "Your updated account balance is $%.2f.%n%nWe appreciate your trust in FinGuard for managing your investments. "
        	        + "%n%nBest regards, %nThe FinGuard Team",
        	        user.getUserName(),
        	        portfolioDto.getPurchasePrice(),
        	        user.getWallet()
        	    )
        	);        
        
//        notificationClient.sendWalletUpdateEmail(emailResponseSender);
        
        logger.info("Transaction record created for user ID {}. Transaction amount: {}", portfolioDto.getUserId(), portfolioDto.getPurchasePrice());
        
        logger.info("Portfolio transaction completed successfully for user ID: {}", portfolioDto.getUserId());
    }


	@Override
	public List<Transaction> monthlyExpense(BudgetReportRequest budgetReportRequest) {
		List<Transaction> transactions = transactionRepository.findByTxnDateBetween(budgetReportRequest.getStartDate(), budgetReportRequest.getEndDate());
		return transactions;
	}

}
