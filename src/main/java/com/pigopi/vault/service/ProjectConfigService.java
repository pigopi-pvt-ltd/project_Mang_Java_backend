
package com.pigopi.vault.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pigopi.vault.dto.ProjectConfigRequestDTO;
import com.pigopi.vault.entity.Project;
import com.pigopi.vault.entity.ProjectConfig;
import com.pigopi.vault.entity.Role;
import com.pigopi.vault.exception.ResourceAlreadyExistException;
import com.pigopi.vault.exception.ResourceNotFoundException;
import com.pigopi.vault.exception.UnAuthorizedException;
import com.pigopi.vault.repository.ProjectConfigRepository;
import com.pigopi.vault.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectConfigService {

	private final ProjectConfigRepository projectConfigRepository;
	private final ProjectRepository projectRepository;

	// CREATE CONFIG

	@Transactional
	public ProjectConfig createConfig(Long projectId, Long currentUserId, Role currentUserRole,
			ProjectConfigRequestDTO requestDTO) {

		// Only SuperAdmin and Manager can create config
		checkWritePermission(currentUserRole);

		// Find project
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

		// Check duplicate config name inside same project
		if (projectConfigRepository.existsByProjectIdAndConfigName(projectId, requestDTO.getConfigName())) {
			throw new ResourceAlreadyExistException(
					"Config with name " + requestDTO.getConfigName() + " already exists in this project");
		}

		ProjectConfig config = ProjectConfig.builder().configName(requestDTO.getConfigName().trim())
				.configValue(requestDTO.getConfigValue()).project(project).build();

		return projectConfigRepository.save(config);
	}

	// GET ALL CONFIGS OF PROJECT
	public List<ProjectConfig> getProjectAllConfigs(Long projectId, Long currentUserId, Role currentUserRole) {

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

		// Employee can only view assigned projects
		checkProjectAccess(projectId, currentUserId, currentUserRole);

		return projectConfigRepository.findByProjectId(project.getId());
	}

	// GET SINGLE CONFIG
	public ProjectConfig getConfig(Long configId, Long currentUserId, Role currentUserRole) {

		ProjectConfig config = projectConfigRepository.findById(configId)
				.orElseThrow(() -> new ResourceNotFoundException("Project config not found with id: " + configId));

		Long projectId = config.getProject().getId();

		// Employee can only view assigned project
		checkProjectAccess(projectId, currentUserId, currentUserRole);

		return config;
	}

	// UPDATE CONFIG
	@Transactional
	public ProjectConfig updateConfig(Long configId, Long currentUserId, Role currentUserRole,
			ProjectConfigRequestDTO requestDTO) {

		// Only SuperAdmin and Manager can update
		checkWritePermission(currentUserRole);

		ProjectConfig config = projectConfigRepository.findById(configId)
				.orElseThrow(() -> new ResourceNotFoundException("Project config not found with id: " + configId));

		Long projectId = config.getProject().getId();

		// Check duplicate config name
		if (!config.getConfigName().equals(requestDTO.getConfigName())) {

			if (projectConfigRepository.existsByProjectIdAndConfigNameAndIdNot(projectId, requestDTO.getConfigName(),
					configId)) {

				throw new ResourceAlreadyExistException(
						"Config with name '" + requestDTO.getConfigName() + "' already exists in this project");
			}
		}

		config.setConfigName(requestDTO.getConfigName().trim());

		config.setConfigValue(requestDTO.getConfigValue());

		return projectConfigRepository.save(config);
	}

	// DELETE CONFIG

	@Transactional
	public void deleteConfig(Long configId, Long currentUserId, Role currentUserRole) {

		// Only SuperAdmin and Manager can delete
		checkWritePermission(currentUserRole);

		ProjectConfig config = projectConfigRepository.findById(configId)
				.orElseThrow(() -> new ResourceNotFoundException("Project config not found with id: " + configId));

		projectConfigRepository.delete(config);
	}

	// WRITE PERMISSION
	private void checkWritePermission(Role role) {

		if (role != Role.SuperAdmin && role != Role.Manager) {

			throw new UnAuthorizedException("Only SuperAdmin and Manager can perform this operation");
		}
	}

	// PROJECT ACCESS
	private void checkProjectAccess(Long projectId, Long currentUserId, Role currentUserRole) {

		// SuperAdmin and Manager can access all projects
		if (currentUserRole == Role.SuperAdmin || currentUserRole == Role.Manager) {
			return;
		}

		// Employee must be assigned to project
		boolean assigned = projectRepository.existsByIdAndUsers_Id(projectId, currentUserId);

		if (!assigned) {
			throw new UnAuthorizedException("You are not assigned to this project");
		}
	}
}
