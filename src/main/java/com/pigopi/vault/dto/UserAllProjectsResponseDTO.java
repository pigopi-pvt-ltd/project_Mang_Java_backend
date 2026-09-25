package com.pigopi.vault.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAllProjectsResponseDTO {
	 private Long userId;

	    private String username;

	    private Set<ProjectResponseDTO> projects;

	    private String message;
}
