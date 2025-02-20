package com.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class EmailResponse {
	private String toEmail;
	private String subject;
	private String message;
}
