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
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequestDTO {
	
	@NotBlank(message = "Name is required !")
	@Size(min = 3, message = "Username must be more than 3 characters")
	private String username;
	
	@NotBlank(message = "Email is required !")
	@Email(message = "Email format isn,t Valid")
	private String email;
	
	@NotBlank(message = "Password is required !")
	@Size(min = 6, message = "Password Must be at least 6 character")
	private String password;
	
	@NotNull(message = "Role is required !")
	private Role role;
	
	@NotBlank(message = "Mobile No. is required !")
	@Pattern(
		    regexp = "^[6-9][0-9]{9}$",
		    message = "Mobile No. must be a valid 10-digit Indian mobile number"
		)
	private String mobilenumber;
	
	@Pattern(
		    regexp = "^(https?://)?(www\\.)?github\\.com/[A-Za-z0-9-]+/?$",
		    message = "Invalid GitHub profile URL"
		)
	private String github;
	
	@Email(message = "Official email format is invalid")
	private String officialEmail;
	
	private String address;
}
