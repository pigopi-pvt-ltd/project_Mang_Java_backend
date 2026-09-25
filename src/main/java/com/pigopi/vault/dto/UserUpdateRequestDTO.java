package com.pigopi.vault.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequestDTO {

	@Size(min = 3, message = "Username must be more than 3 characters")
	private String username;

	@Email(message = "Invalid Email format")
	private String email;

	@Pattern(regexp = "^[6-9][0-9]{9}$", message = "Mobile number must be a valid 10-digit Indian mobile number")
	private String mobilenumber;

	@Pattern(regexp = "^(https?://)?(www\\.)?github\\.com/[A-Za-z0-9-]+/?$", message = "Invalid GitHub URL")
	private String github;

	@Email(message = "Invalid official email format")
	private String officialEmail;

	@Size(max = 500, message = "Address cannot exceed 500 characters")
	private String address;
}
