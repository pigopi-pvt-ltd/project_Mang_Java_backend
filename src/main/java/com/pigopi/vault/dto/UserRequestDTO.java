package com.pigopi.vault.dto;

import com.pigopi.vault.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

	@NotBlank(message = "Username is required ! ")
	@Size(min = 3, message = "Username must be more than 3 characters")
	private String username;

	@NotBlank(message = "Email is required ! ")
	@Email(message = "Email format isn,t Valid")
	private String email;

	@NotBlank(message = "Password is required ! ")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	@NotNull(message = "Role is required ! ")
	private Role role;

	@NotBlank(message = "Mobile number is required ! ")
	@Pattern(regexp = "^[6-9][0-9]{9}$", message = "Mobile number must be a valid 10-digit Indian mobile number")
	private String mobileNumber;

	@Pattern(regexp = "^(https?://)?(www\\.)?github\\.com/[A-Za-z0-9-]+/?$", message = "Invalid GitHub profile URL")
	private String github;

	@Email(message = "Invalid official email format")
	private String officialEmail;

	private String address;
}
