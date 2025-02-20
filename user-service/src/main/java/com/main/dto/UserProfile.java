package com.main.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfile 
{	
	private int userId;
	private String userName;
	private String email;
	private String role;
	private BigDecimal wallet;
}
