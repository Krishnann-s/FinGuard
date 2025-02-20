package com.main.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransactionDto {
	private int userId;
	private BigDecimal amount;
	private BigDecimal wallet;
	private String txnType;
}
