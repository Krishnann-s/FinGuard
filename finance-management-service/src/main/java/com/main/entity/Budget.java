package com.main.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Budget {
	@Id
	@GeneratedValue
	private int budgetId;
	
	@NotNull(message="user ID cannot be null")
    private int userId;
	@NotNull(message="Amount cannot be null")
	@Digits(integer = 6, fraction = 2)
	@PositiveOrZero
    private BigDecimal amount;
	@PositiveOrZero
	@Digits(integer = 6, fraction = 2)
    private BigDecimal spentAmount;
	@NotEmpty(message="Catagory cannot be null")
    private String category;
	@NotNull(message="Start date cannot be null")
    private LocalDate startDate;
	@NotNull(message=" End date cannot be null")
    private LocalDate endDate;
	@CreationTimestamp
	private LocalDate createdAt;
	@UpdateTimestamp
	private LocalDate updatedAt;
}



