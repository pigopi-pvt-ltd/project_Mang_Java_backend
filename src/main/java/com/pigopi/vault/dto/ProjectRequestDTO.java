package com.pigopi.vault.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectRequestDTO {
	@NotNull(message = "Project Name Required!")
	@Size(min = 3, message = "Username must be more than 3 characters")
	private String name;
	@Size(max = 2000, message = "Description must be less than 2000 characters")
	private String description;
}
