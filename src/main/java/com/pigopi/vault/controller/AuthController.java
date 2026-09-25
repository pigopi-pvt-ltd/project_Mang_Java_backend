package com.pigopi.vault.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pigopi.vault.dto.AuthResponseDTO;
import com.pigopi.vault.dto.ChangePasswordRequestDTO;
import com.pigopi.vault.dto.ForgotPasswordRequestDTO;
import com.pigopi.vault.dto.LoginRequestDTO;
import com.pigopi.vault.dto.ResetPasswordRequestDTO;
import com.pigopi.vault.entity.Role;
import com.pigopi.vault.entity.User;
import com.pigopi.vault.exception.ResourceNotFoundException;
import com.pigopi.vault.service.AuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO request, HttpSession session) {

		User user = authService.login(request.getEmail(), request.getPassword());

		session.setAttribute("userId", user.getId());
		session.setAttribute("userName", user.getUsername());
		session.setAttribute("userEmail", user.getEmail());
		session.setAttribute("userRole", user.getRole());
		AuthResponseDTO authDTO = AuthResponseDTO.builder().id(user.getId()).name(user.getUsername())
				.email(user.getEmail()).role(user.getRole()).mobileNumber(user.getMobileNumber())
				.github(user.getGithub()).address(user.getAddress()).officialEmail(user.getOfficialEmail())
				.message("Login Successful").build();

		return ResponseEntity.ok(authDTO);
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpSession session) {
		authService.logout(session);
		return ResponseEntity.ok("Logout successfully");
	}

	@GetMapping("/me")
	public ResponseEntity<AuthResponseDTO> currentUser(HttpSession session) {

		Long userId = (Long) session.getAttribute("userId");

		if (userId == null) {
			throw new ResourceNotFoundException("Not logged in");
		}
		AuthResponseDTO currentUserDTO = AuthResponseDTO.builder().id(userId)
				.name((String) session.getAttribute("userName")).email((String) session.getAttribute("userEmail"))
				.role((Role) session.getAttribute("userRole")).message("Current User Details").build();
		return ResponseEntity.ok(currentUserDTO);
	}

	// CHANGE PASSWORD
	@PutMapping("/change-password")
	public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequestDTO requestDTO,
			HttpSession session) {

		Long userId = (Long) session.getAttribute("userId");

		if (userId == null) {
			return ResponseEntity.status(401).body("Please login first");
		}

		authService.changePassword(userId, requestDTO);

		return ResponseEntity.ok("Password updated successfully");
	}

	// FORGOT PASSWORD
	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO requestDTO) {

		authService.forgotPassword(requestDTO.getEmail());

		return ResponseEntity.ok("Password reset instructions sent have been sent to your email");
	}

	// RESET PASSWORD
	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO requestDTO) {

		authService.resetPassword(requestDTO);

		return ResponseEntity.ok("Password reset successfully");
	}
}
