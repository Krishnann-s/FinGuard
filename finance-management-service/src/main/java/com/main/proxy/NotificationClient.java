package com.main.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.main.dto.EmailResponse;

@FeignClient("notification-service")
public interface NotificationClient {
	@PostMapping("/notification/sendWalletUpdateEmail")
	Void sendWalletUpdateEmail(EmailResponse emailResponse);
}
