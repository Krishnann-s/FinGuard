package com.main.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWallet {
//	@Digits(integer= 6, fraction= 2)
	private BigDecimal amount;
}
