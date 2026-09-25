package com.pigopi.vault.dto;

import com.pigopi.vault.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDTO {
	private Long id;
	private String name;
	private String email;
	private Role role;
	private String mobileNumber;
	private String github;
	private String officialEmail;
	private String address;
	private String message;
}
