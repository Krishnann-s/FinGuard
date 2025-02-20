package com.main.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.cglib.core.Local;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PortfolioResponse {

	private int investmentId;
    private int userId;
    private String assetType;
    private int quantity;
    private BigDecimal purchasePrice;
    private BigDecimal currentPrice;
    private LocalDate purchaseDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
