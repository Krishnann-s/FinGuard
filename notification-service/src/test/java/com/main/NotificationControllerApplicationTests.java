package com.main;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.MailException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.controller.NotificationController;
import com.main.dto.EmailRequest;
import com.main.service.NotificationService;

@WebMvcTest(NotificationController.class)
public class NotificationControllerApplicationTests {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    public void testSendEmail_Positive() throws Exception {
        // Arrange
        EmailRequest emailRequest = new EmailRequest("test@example.com", "Subject", "Body");
        // Do nothing when the service is called
        doNothing().when(notificationService).sendEmail(any(EmailRequest.class));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders
                .post("/notification/sendEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(emailRequest)))
                .andExpect(status().isOk());

        // Verify that the sendEmail method in the service was called once
        verify(notificationService, times(1)).sendEmail(any(EmailRequest.class));
    }
    
    @Test
    public void testSendEmail_Negative() throws Exception {
        EmailRequest emailRequest = new EmailRequest("test@example.com", "Subject", "Body");

        // Simulate a failure in sending email by throwing an exception
        doThrow(new RuntimeException("Email sending failed!")).when(notificationService).sendEmail(any(EmailRequest.class));

        mockMvc.perform(post("/notification/sendEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"toEmail\": \"test@example.com\", \"subject\": \"Subject\", \"body\": \"Body\"}"))
                .andExpect(status().isInternalServerError());  // Expecting 500 Internal Server Error
    }
}
