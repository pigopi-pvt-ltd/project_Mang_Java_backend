package com.pigopi.vault.dto;

import com.pigopi.vault.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDTO {
	private Long id;
	private String username;
	private String email;
	private Role role;
	private String mobileNumber;
	private String github;
	private String officialEmail;
	private String address;
	private String message;
}
