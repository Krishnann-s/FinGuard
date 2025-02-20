package com.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class NotificationDTO 
{
	private int notificationId;
	private int userId;
	private String notificationType; // sms, email
	private String message;
	private String status; // sent, pending, failed
	private String triggerEvent;
}
