package com.main.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
public class Portfolio {
	@Id
	@GeneratedValue
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



