
package com.pigopi.vault.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectFileResponseDTO {

	private Long id;

	private String fileName;

	private String fileType;

	private Long fileSize;

	private String cloudinaryUrl;

	private Long projectId;
	
	private String resourceType;
	
	private Long uploadedByUserId;
		
	private LocalDateTime uploadedAt;
}
