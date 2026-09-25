package com.pigopi.vault.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EmailService {
	
	@Value("${app.frontend.url}")
    private String frontendUrl;
	
	private final JavaMailSender mailSender;

    
	private void sendEmail(String to, String subject, String body) {

		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
	}

	// 1. Account created
	public void sendAccountCreatedEmail(String email, String username) {

		String subject = "PiGoPi Vault - Account Created";

		String body = "Hello " + username + ",\n\n" +
				"Your PiGoPi Vault account has been created successfully.\n\n" +
				"Username: " + username + "\n" + "Email: " + email + "\n\n" +
				"You can now log in to PiGoPi Vault.\n\n" +
				"For security reasons, please do not share your password.\n\n" +
				"Regards,\n" + "PiGoPi Vault Team";
		sendEmail(email, subject, body);
	}

	// 2. Password updated
	public void sendPasswordUpdatedEmail(String email, String username) {

		String subject = "PiGoPi Vault - Password Updated";
		String body = "Hello " + username + ",\n\n" +
				"Your PiGoPi Vault password has been updated successfully.\n\n" +
				"If you made this change, no further action is required.\n\n" +
				"If you did not make this change, please contact the administrator immediately.\n\n" +
				"Regards,\n" + "PiGoPi Vault Team";
		sendEmail(email, subject, body);
	}

	// 3. Forgot password - send reset token
	public void sendPasswordResetEmail(String email, String username,String tokenId, String token) {

		String subject = "PiGoPi Vault - Password Reset";
		String resetLink =   frontendUrl
	            + "/reset-password?tokenId="
	            + tokenId
	            + "&token="
	            + token;
		String body = "Hello " + username + ",\n\n" +
				"We received a request to reset your PiGoPi Vault password.\n\n" +
				"Your password reset token is:\n\n" + token + "\n\n" +
				"Reset your password using this link:\n" + resetLink + "\n\n" +
				"This token will expire in 15 minutes.\n\n" +
				"If you did not request a password reset, please ignore this email.\n\n" +
				"Regards,\n" + "PiGoPi Vault Team";

		sendEmail(email, subject, body);
	}

	// 4. Password reset successfully
	public void sendPasswordResetSuccessEmail(String email, String username) {

		String subject = "PiGoPi Vault - Password Reset Successful";

		String body = "Hello " + username + ",\n\n" +
				"Your PiGoPi Vault password has been reset successfully.\n\n" +
				"If you did not perform this action, please contact the administrator immediately.\n\n" +
				"Regards,\n" + "PiGoPi Vault Team";
		sendEmail(email, subject, body);
	}

	// 5. Project assigned
	public void sendProjectAssignedEmail(String email, String username, String projectName) {

		String subject = "PiGoPi Vault - Project Assigned";
		String body = "Hello " + username + ",\n\n" +
				"You have been assigned to the following project:\n\n" +
				"Project: " + projectName + "\n\n" +
				"You can now access the project according to your assigned permissions.\n\n" +
				"Regards,\n" + "PiGoPi Vault Team";
		sendEmail(email, subject, body);
	}
}