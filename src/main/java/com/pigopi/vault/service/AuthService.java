package com.pigopi.vault.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pigopi.vault.dto.ChangePasswordRequestDTO;
import com.pigopi.vault.dto.ResetPasswordRequestDTO;
import com.pigopi.vault.entity.PasswordResetToken;
import com.pigopi.vault.entity.User;
import com.pigopi.vault.exception.ResourceNotFoundException;
import com.pigopi.vault.exception.UnAuthorizedException;
import com.pigopi.vault.repository.PasswordResetTokenRepository;
import com.pigopi.vault.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	public User login(String email, String password) {
		User user = userRepository.findByEmail(email).orElse(null);
		if (user == null) {
			throw new UnAuthorizedException("Invaild Email or Password");
		}
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new UnAuthorizedException("Invaild Email or Password");
		}
		return user;
	}

	public void logout(HttpSession session) {
		session.invalidate();
	}

	// CHANGE PASSWORD
	public void changePassword(Long userId, ChangePasswordRequestDTO requestDTO) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

		// Check current password
		if (!passwordEncoder.matches(requestDTO.getCurrentPassword(), user.getPassword())) {

			throw new UnAuthorizedException("Current password is incorrect");
		}
		// Check new password and confirm password
		if (!requestDTO.getNewPassword().equals(requestDTO.getConfirmPassword())) {
			throw new IllegalArgumentException("New password and confirm password do not match");
		}

		// Encode new password
		user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));

		userRepository.save(user);
		// Send email
		emailService.sendPasswordUpdatedEmail(user.getEmail(), user.getUsername());
	}

//forget via email
	public void forgotPassword(String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with this email"));

		// Remove old token
		passwordResetTokenRepository.deleteByUserId(user.getId());

		// Generate token
		String token = UUID.randomUUID().toString();
		 // Generate separate ID for database lookup
        String tokenId = UUID.randomUUID().toString();
		PasswordResetToken resetToken = PasswordResetToken.builder().token(passwordEncoder.encode(token)).user(user)
				.tokenId(tokenId).expiryTime(LocalDateTime.now().plusMinutes(15)).build();

		passwordResetTokenRepository.save(resetToken);

		emailService.sendPasswordResetEmail(user.getEmail(), user.getUsername(), tokenId,token);
		System.out.println("Password reset token: " + token);
	}

	public void resetPassword(ResetPasswordRequestDTO requestDTO) {

		PasswordResetToken resetToken = passwordResetTokenRepository
				.findByTokenId(requestDTO.getTokenId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid reset token"));

		if (resetToken.getExpiryTime().isBefore(LocalDateTime.now())) {
			  passwordResetTokenRepository
              .delete(resetToken);
			throw new IllegalArgumentException("Reset token has expired");
		}
		

		 boolean tokenMatches =
	                passwordEncoder.matches(
	                        requestDTO.getToken(),
	                        resetToken.getToken());
		 if (!tokenMatches) {
	            throw new UnAuthorizedException(
	                    "Invalid reset token");
	        }if (!requestDTO.getNewPassword().equals(requestDTO.getConfirmPassword())) {
				throw new IllegalArgumentException("Passwords do not match");
			}
		User user = resetToken.getUser();
		user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));

		userRepository.save(user);

		// Token can be used only once
		passwordResetTokenRepository.delete(resetToken);
		// Send success email
		emailService.sendPasswordResetSuccessEmail(user.getEmail(), user.getUsername());
	}
}
