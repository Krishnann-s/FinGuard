package com.main.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateRequest {
	
	@NotNull(message = "username cannot be empty")
	@Size(min=3, max=12)
	private String userName;
	
	@NotNull(message = "Email Id should not be empty")
	@Email
	private String email;
	
	@NotNull(message = "Role cannot be empty")
	@Pattern(regexp = "user|admin")
	private String role; // role can be updated by admin
	
	@PositiveOrZero
	@Digits(integer = 6, fraction = 2)
	private BigDecimal wallet = BigDecimal.ZERO;
}
