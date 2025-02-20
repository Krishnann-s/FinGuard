package com.main.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebtTxn {

    @NotNull(message = "Loan ID cannot be null")
    @Min(value = 1, message = "Loan ID must be greater than 0")
    private Integer loanId;

    @NotNull(message = "Amount cannot be null")
    @Min(value = 0, message = "Amount must be non-negative")
    private BigDecimal amount;
}