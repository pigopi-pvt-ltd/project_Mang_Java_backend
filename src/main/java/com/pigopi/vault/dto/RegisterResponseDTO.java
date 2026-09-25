package com.pigopi.vault.dto;

import com.pigopi.vault.entity.Role;

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
public class RegisterResponseDTO {

	private Long id;
	private String username;
	private String email;
	private Role role;
	private String message;

}
