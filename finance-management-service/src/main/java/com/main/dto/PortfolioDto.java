package com.main.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PortfolioDto {

    @NotNull(message = "User ID cannot be null")
    @Min(value = 1, message = "User ID must be greater than 0")
    private Integer userId;

    @NotNull(message = "Asset type cannot be null")
    @Size(min = 1, max = 50, message = "Asset type must be between 1 and 50 characters")
    private String assetType;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Purchase price cannot be null")
    @Min(value = 0, message = "Purchase price must be non-negative")
    private BigDecimal purchasePrice;

    @NotNull(message = "Current price cannot be null")
    @Min(value = 0, message = "Current price must be non-negative")
    private BigDecimal currentPrice;

    @NotNull(message = "Purchase date cannot be null")
    @PastOrPresent(message = "Purchase date cannot be in the future")
    private LocalDate purchaseDate;
}