package com.pigopi.vault.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.pigopi.vault.entity.Project;
import com.pigopi.vault.entity.ProjectFile;
import com.pigopi.vault.entity.Role;
import com.pigopi.vault.entity.User;
import com.pigopi.vault.exception.ResourceNotFoundException;
import com.pigopi.vault.exception.UnAuthorizedException;
import com.pigopi.vault.repository.ProjectFileRepository;
import com.pigopi.vault.repository.ProjectRepository;
import com.pigopi.vault.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectFileService {
	private final ProjectFileRepository projectFileRepository;
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;
	private final Cloudinary cloudinary;

	// UPLOAD
	@Transactional
	public ProjectFile uploadFile(Long projectId, Long userId, Role userRole, MultipartFile file) throws IOException {
		if (userRole != Role.SuperAdmin && userRole != Role.Manager) {
			throw new UnAuthorizedException("Only SuperAdmin and Manager can upload files");
		}
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("File cannot be empty");
		}
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

		// 5. Upload to Cloudinary //
		String folder = "pigo-vault/projects/" + projectId;

		Map<String, Object> uploadOptions = new HashMap<>();
		uploadOptions.put("folder", folder);
		uploadOptions.put("resource_type", "auto");

		@SuppressWarnings("unchecked")
		Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadOptions);

		// 6. Get Cloudinary Response
		String publicId = (String) uploadResult.get("public_id");
		String secureUrl = (String) uploadResult.get("secure_url");
		String resourceType = (String) uploadResult.get("resource_type");

		// 7. Save Metadata in MySQL
		ProjectFile projectFile = ProjectFile.builder().fileName(file.getOriginalFilename())
				.fileType(file.getContentType()).fileSize(file.getSize()).cloudinaryUrl(secureUrl).publicId(publicId)
				.resourceType(resourceType).project(project).uploadedBy(user).build();
		return projectFileRepository.save(projectFile);
	}

	// GET PROJECT FILES
	public List<ProjectFile> getProjectFiles(Long projectId, Long userId, Role userRole) {
		if (!projectRepository.existsById(projectId)) {
			throw new ResourceNotFoundException("Project not found with id: " + projectId);
		}

		if (userRole == Role.Employee) {
			boolean assigned = projectRepository.existsByIdAndUsers_Id(projectId, userId);
			if (!assigned) {
				throw new UnAuthorizedException("You are not assigned to this project");
			}
		}
		return projectFileRepository.findByProjectId(projectId);
	}

	// GET FILE
	public ProjectFile getFile(Long fileId, Long userId, Role userRole) {
		ProjectFile projectFile = projectFileRepository.findById(fileId)
				.orElseThrow(() -> new ResourceNotFoundException("Project file not found with id: " + fileId));
		Project project = projectFile.getProject();

		// Employee access check
		if (userRole == Role.Employee) {
			boolean assigned = projectRepository.existsByIdAndUsers_Id(project.getId(), userId);
			if (!assigned) {
				throw new UnAuthorizedException("You are not assigned to this project");
			}
		}
		return projectFile;
	}

	// DELETE FILE
	@Transactional
	public void deleteFile(Long fileId, Role userRole) throws Exception {
		// 1. Check Role
		if (userRole != Role.SuperAdmin && userRole != Role.Manager) {
			throw new UnAuthorizedException("Only SuperAdmin and Manager can delete files");
		}
		// 2. Find DB record //
		ProjectFile projectFile = projectFileRepository.findById(fileId)
				.orElseThrow(() -> new ResourceNotFoundException("Project file not found with id: " + fileId));

		// 3. Delete from Cloudinary //

		Map<String, Object> destroyOptions = new HashMap<>();
		destroyOptions.put("resource_type", projectFile.getResourceType());
		destroyOptions.put("invalidate", true);
		cloudinary.uploader().destroy(projectFile.getPublicId(), destroyOptions);
		// 4. Delete from MySQL
		projectFileRepository.delete(projectFile);
	}
}
