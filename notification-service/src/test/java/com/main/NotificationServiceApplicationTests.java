package com.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.main.dto.EmailRequest;
import com.main.service.NotificationService;

@SpringBootTest
class NotificationServiceApplicationTests {

	@Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    public void testSendEmail_Positive() {
        // Arrange
        EmailRequest emailRequest = new EmailRequest("test@example.com", "Subject", "Body");

        // Act
        notificationService.sendEmail(emailRequest);

        // Assert
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
    
    @Test
    public void testSendEmail_Negative() {
        // Arrange
        EmailRequest emailRequest = new EmailRequest("test@example.com", "Subject", "Body");
        doThrow(new MailException("Email sending failed!") {}).when(javaMailSender).send(any(SimpleMailMessage.class));

        // Act & Assert
        try {
            notificationService.sendEmail(emailRequest);
        } catch (MailException ex) {
            // Assert that the exception was thrown
            assertEquals("Email sending failed!", ex.getMessage());
        }

        // Verify that the mail sender was still called once, but the email sending failed
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
