package com.main.dto;

import lombok.Data;

@Data
public class TransferObj {
	
	private String senderMail;
	private String userName;
	private Double amount;
	private String status;

}
