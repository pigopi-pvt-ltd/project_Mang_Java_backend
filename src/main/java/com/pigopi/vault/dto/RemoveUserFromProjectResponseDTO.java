package com.pigopi.vault.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RemoveUserFromProjectResponseDTO {

	private Long userId;

	private Long projectId;

	private String username;
	private String projectName;
	private String message;
}