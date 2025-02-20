package com.main.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest 
{
	@NotNull(message = "username cannot be empty")
	@Size(min=3, max=12)
	private String userName;
	
	@NotNull(message = "Password should not be empty")
	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^,.])(?=\\S+$).{8,20}$")
	private String password;
	
	@NotNull(message = "Email Id should not be empty")
	@Email
	private String email;
	
	 @Pattern(regexp = "user|admin", message = "Role must be either 'user' or 'admin'")
	   private String role;
}
