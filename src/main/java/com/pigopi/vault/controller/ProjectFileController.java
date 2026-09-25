package com.pigopi.vault.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pigopi.vault.dto.ProjectFileResponseDTO;
import com.pigopi.vault.entity.ProjectFile;
import com.pigopi.vault.entity.Role;
import com.pigopi.vault.service.ProjectFileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projectfiles")
@RequiredArgsConstructor
public class ProjectFileController {

	private final ProjectFileService projectFileService;

	// UPLOAD FILE Only SuperAdmin + Manager
	@PostMapping("/project/{projectId}")
	public ResponseEntity<ProjectFileResponseDTO> uploadFile(@PathVariable Long projectId,
			@RequestParam MultipartFile file, @RequestAttribute Long currentUserId,
			@RequestAttribute Role currentUserRole) throws Exception {

		ProjectFile savedFile = projectFileService.uploadFile(projectId, currentUserId, currentUserRole, file);
		ProjectFileResponseDTO respFile = ProjectFileResponseDTO.builder().id(savedFile.getId())
				.fileName(savedFile.getFileName()).fileType(savedFile.getFileType()).fileSize(savedFile.getFileSize())
				.cloudinaryUrl(savedFile.getCloudinaryUrl()).projectId(projectId)
				.resourceType(savedFile.getResourceType()).uploadedByUserId(savedFile.getUploadedBy().getId())
				.uploadedAt(savedFile.getUploadedAt()).build();
		return ResponseEntity.ok(respFile);
	}

	// GET ALL PROJECT FILES
	// SuperAdmin -> Any project
	// Manager -> Any project
	// Employee -> Assigned project only

	@GetMapping("/project/{projectId}")
	public ResponseEntity<List<ProjectFileResponseDTO>> getProjectFiles(@PathVariable Long projectId,
			@RequestAttribute Long currentUserId, @RequestAttribute Role currentUserRole) {

		List<ProjectFile> files = projectFileService.getProjectFiles(projectId, currentUserId, currentUserRole);
		List<ProjectFileResponseDTO> allFiles = files.stream().map(savedFile -> ProjectFileResponseDTO.builder()
				.id(savedFile.getId()).fileName(savedFile.getFileName()).fileType(savedFile.getFileType())
				.fileSize(savedFile.getFileSize()).cloudinaryUrl(savedFile.getCloudinaryUrl()).projectId(projectId)
				.resourceType(savedFile.getResourceType()).uploadedByUserId(savedFile.getUploadedBy().getId())
				.uploadedAt(savedFile.getUploadedAt()).build()
		).toList();

		return ResponseEntity.ok(allFiles);
	}

	// GET ONE FILE
	// SuperAdmin -> Any file
	// Manager -> Any file
	// Employee -> Assigned project only
	@GetMapping("/{fileId}")
	public ResponseEntity<ProjectFileResponseDTO> getFile(@PathVariable Long fileId, @RequestAttribute Long currentUserId,
			@RequestAttribute Role currentUserRole) {

		ProjectFile projectFile = projectFileService.getFile(fileId, currentUserId, currentUserRole);
		
		 ProjectFileResponseDTO response =
		            ProjectFileResponseDTO.builder()
		                    .id(projectFile.getId())
		                    .fileName(projectFile.getFileName())
		                    .fileType(projectFile.getFileType())
		                    .fileSize(projectFile.getFileSize())
		                    .cloudinaryUrl(projectFile.getCloudinaryUrl())
		                    .projectId(projectFile.getProject().getId())
		                    .resourceType(projectFile.getResourceType())
		                    .uploadedByUserId(projectFile.getUploadedBy().getId())
		                    .uploadedAt(projectFile.getUploadedAt())
		                    .build();
		
		return ResponseEntity.ok(response);
	}

	// DELETE FILE
	// SuperAdmin + Manager

	@DeleteMapping("/{fileId}")
	public ResponseEntity<String> deleteFile(@PathVariable Long fileId, @RequestAttribute Role currentUserRole)
			throws Exception {
		projectFileService.deleteFile(fileId, currentUserRole);
		return ResponseEntity.ok("Project file deleted successfully");
	}
}
