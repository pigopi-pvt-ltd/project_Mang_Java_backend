package com.pigopi.vault.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
	 @NotBlank(message = "Email is required !")
		@Email(message = "Email format isn,t Valid")
		private String email;
		@NotBlank(message = "Password is required !")
		private String password;
}
