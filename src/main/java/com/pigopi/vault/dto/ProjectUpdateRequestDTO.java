package com.pigopi.vault.dto;

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
public class ProjectUpdateRequestDTO {

	@Size(min = 3, message = "Project name must be more than 3 characters")
	private String name;

	@Size(max = 2000, message = "Description cannot exceed 2000 characters")
	private String description;
}
