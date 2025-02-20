package com.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WalletUpdateEmailRequest {

	private String toEmail;
	private String subject;
	private String message;
}
