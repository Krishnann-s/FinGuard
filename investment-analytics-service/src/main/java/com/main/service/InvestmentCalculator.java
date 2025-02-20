package com.main.service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class InvestmentCalculator {

    public static void calculateInvestment(String assetType, int investmentDuration, double investmentAmount, String userEmail) {
        double interestRate = calculateInterestRate(assetType, investmentDuration);
        double totalReturn = calculateTotalReturn(investmentAmount, interestRate);
        
        System.out.printf("For an investment of %.2f in %s for %d year(s), the total return is: %.2f\n", 
                          investmentAmount, assetType, investmentDuration, totalReturn);
        
        // Send email notification
        sendEmailNotification(userEmail, assetType, investmentDuration, investmentAmount, totalReturn);
    }

    public static double calculateInterestRate(String assetType, int duration) {
        if (assetType.equals("mutual funds")) {
            if (duration == 1) {
                return 0.04; // 4% interest for 1 year
            } else if (duration == 3) {
                return 0.12; // 12% interest for 3 years
            } else {
                throw new IllegalArgumentException("Investment duration must be either 1 or 3 years.");
            }
        } else {
            throw new IllegalArgumentException("Invalid asset type. Only 'mutual funds' is supported.");
        }
    }

    public static double calculateTotalReturn(double amount, double interestRate) {
        return amount + (amount * interestRate); // Total return calculation
    }

    private static void sendEmailNotification(String to, String assetType, int duration, double amount, double totalReturn) {
        // Set up the email server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.example.com"); // Replace with your SMTP server
        properties.put("mail.smtp.port", "587"); // Replace with your SMTP port

        // Create a session with an authenticator
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("your_email@example.com", "your_password"); // Replace with your email and password
            }
        });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("your_email@example.com")); // Replace with your email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Investment Notification");
            message.setText(String.format("Dear User,\n\nYou have successfully invested %.2f in %s for %d year(s).\nThe total return will be: %.2f\n\nThank you!", 
                                            amount, assetType, duration, totalReturn));

            // Send the message
            Transport.send(message);
            System.out.println("Email notification sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}