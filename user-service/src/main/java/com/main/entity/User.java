package com.main.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "userlogin")
public class User 
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int userId;
	
	private String userName;
	
	private String password;

	private String email;
	
	private String role;
	

	@PositiveOrZero
	@Digits(integer = 6, fraction = 2)
	private BigDecimal wallet = BigDecimal.ZERO;
	
	@CreationTimestamp
	@PastOrPresent
	private LocalDate createdAt;
	
	@UpdateTimestamp
	private LocalDate updatedAt;
	
}
