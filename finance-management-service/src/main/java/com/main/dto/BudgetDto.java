
//package com.main.dto;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//import jakarta.validation.constraints.Digits;
//import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.PositiveOrZero;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class BudgetDto {
//	
//	@NotNull(message="user ID cannot be null")
//    private int userId;
//	@NotNull(message="Amount cannot be null")
//	@Digits(integer = 6, fraction = 2)
//	@PositiveOrZero
//    private BigDecimal amount;
//	@PositiveOrZero
//	@Digits(integer = 6, fraction = 2)
//    private BigDecimal spentAmount;
//	@NotEmpty(message="Catagory cannot be null")
//    private String category;
//	@NotNull(message="Start date cannot be null")
//    private LocalDate startDate;
//	@NotNull(message=" End date cannot be null")
//    private LocalDate endDate;
//
//}