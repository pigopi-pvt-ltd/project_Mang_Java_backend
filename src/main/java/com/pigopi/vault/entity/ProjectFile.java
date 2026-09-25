package com.pigopi.vault.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PROJECT_FILES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String fileName;

	private String fileType;

	private Long fileSize;

	@Column(nullable = false)
	private String cloudinaryUrl;

	@Column(nullable = false)
	private String publicId;
	@Column(nullable = false)
	private String resourceType;

	@Builder.Default
	private LocalDateTime uploadedAt = LocalDateTime.now();

//create a column project_Id in the PROJECT_FILES table to store the ID of the Project to which the file belongs.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	// Create a column called uploaded_by in the PROJECT_FILES table to store the ID
	// of the User who uploaded the file.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uploaded_by")
	private User uploadedBy;
}