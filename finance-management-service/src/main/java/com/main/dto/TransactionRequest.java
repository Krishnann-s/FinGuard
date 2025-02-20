package com.main.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {

    @NotNull(message = "Sender user ID cannot be null")
    @Min(value = 1, message = "Sender user ID must be greater than 0")
    private Integer senderUserId;

    @NotNull(message = "Receiver user ID cannot be null")
    @Min(value = 1, message = "Receiver user ID must be greater than 0")
    private Integer receiverUserId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
}
