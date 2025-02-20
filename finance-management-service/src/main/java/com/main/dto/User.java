package com.main.dto;


import java.math.BigDecimal;

import lombok.Data;

@Data
public class User {
	private int userId;
	private String userName;
	private String email;
	private String role;
	private BigDecimal wallet;
}
