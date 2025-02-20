package com.main.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Debt {
    @Id
    @GeneratedValue
    private int loanId; 

    @NotNull(message = "User ID cannot be null")
    private int userId;
    
    @NotNull(message = "LoanType cannot be null")
    private String loanType; 

//    @NotNull(message = "Principal amount cannot be null")
//    private double principalAmount;
    
    @PositiveOrZero
	@Digits(integer = 6, fraction = 2)
	private BigDecimal principalAmount = BigDecimal.ZERO;

    @NotNull(message = "Interest rate cannot be null")
    private double interestRate;
    
    @PositiveOrZero
	@Digits(integer = 6, fraction = 2)
	private BigDecimal emiAmount = BigDecimal.ZERO;

//    @NotNull(message = "Amount paid cannot be null")
//    private double amountPaid;
    
    @PositiveOrZero
	@Digits(integer = 6, fraction = 2)
	private BigDecimal amountPaid = BigDecimal.ZERO;

    @NotNull(message = "Start date cannot be null")
//    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
//    @PastOrPresent(message = "End date cannot be in the future")
    private LocalDate endDate;

    @CreationTimestamp
//    @PastOrPresent(message = "Creation date cannot be in the future")
    private LocalDate createdAt;

    @UpdateTimestamp
//    @PastOrPresent(message = "Update date cannot be in the future")
    private LocalDate updatedAt;
}