package com.main.model;

import lombok.Data;

@Data
public class TransferObj {
	
	private String senderMail;
	private String userName;
	private Double amount;
	private String status;

}
