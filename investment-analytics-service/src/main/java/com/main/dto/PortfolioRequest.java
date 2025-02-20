package com.main.dto;


import java.math.BigDecimal;
//import java.sql.Date;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class PortfolioRequest {
	 public PortfolioRequest() {
	}
	@NotNull(message = "User  ID cannot be null")
	 private int userId;
	 @NotBlank(message = "Asset type cannot be blank")
     private String assetType;
	 @Min(value = 0, message = "Quantity must be non-negative")
     private int quantity;
	 @Min(value = 0, message = "Purchase price must be non-negative")
     private BigDecimal purchasePrice;
	 @Min(value = 0, message = "Current price must be non-negative")
     private BigDecimal currentPrice;
	 @CreationTimestamp
     private LocalDate purchaseDate;
	 @CreationTimestamp
	 private LocalDate createdAt;
	 @UpdateTimestamp
	 private LocalDate updatedAt;

}
