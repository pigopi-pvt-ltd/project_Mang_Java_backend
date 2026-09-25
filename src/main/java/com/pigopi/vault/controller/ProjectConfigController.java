package com.pigopi.vault.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pigopi.vault.dto.ProjectConfigRequestDTO;
import com.pigopi.vault.dto.ProjectConfigResponseDTO;
import com.pigopi.vault.entity.ProjectConfig;
import com.pigopi.vault.entity.Role;
import com.pigopi.vault.service.ProjectConfigService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/configs")
@RequiredArgsConstructor
public class ProjectConfigController {

	private final ProjectConfigService projectConfigService;

	// Create Config
	@PostMapping("/project/{projectId}")
	public ResponseEntity<ProjectConfigResponseDTO> createConfig(@PathVariable Long projectId,
			@Valid @RequestBody ProjectConfigRequestDTO requestDTO, @RequestAttribute Long currentUserId,
			@RequestAttribute Role currentUserRole) {

		ProjectConfig savedConfig = projectConfigService.createConfig(projectId, currentUserId, currentUserRole,
				requestDTO);

		ProjectConfigResponseDTO response = ProjectConfigResponseDTO.builder().id(savedConfig.getId())
				.configName(savedConfig.getConfigName()).configValue(savedConfig.getConfigValue())
				.projectId(savedConfig.getProject().getId()).createdAt(savedConfig.getCreatedAt())
				.updatedAt(savedConfig.getUpdatedAt()).build();

		return ResponseEntity.ok(response);
	}

	// Get all configs of a project
	@GetMapping("/project/{projectId}")
	public ResponseEntity<List<ProjectConfigResponseDTO>> getProjectAllConfigs(@PathVariable Long projectId,
			@RequestAttribute Long currentUserId, @RequestAttribute Role currentUserRole) {

		List<ProjectConfig> configs = projectConfigService.getProjectAllConfigs(projectId, currentUserId,
				currentUserRole);

		List<ProjectConfigResponseDTO> response = configs.stream()
				.map(config -> ProjectConfigResponseDTO.builder().id(config.getId()).configName(config.getConfigName())
						.configValue(config.getConfigValue()).projectId(config.getProject().getId())
						.createdAt(config.getCreatedAt()).updatedAt(config.getUpdatedAt()).build())
				.toList();

		return ResponseEntity.ok(response);
	}

	// Get single config
	@GetMapping("/{configId}")
	public ResponseEntity<ProjectConfigResponseDTO> getConfig(@PathVariable Long configId,
			@RequestAttribute Long currentUserId, @RequestAttribute Role currentUserRole) {

		ProjectConfig config = projectConfigService.getConfig(configId, currentUserId, currentUserRole);

		ProjectConfigResponseDTO response = ProjectConfigResponseDTO.builder().id(config.getId())
				.configName(config.getConfigName()).configValue(config.getConfigValue())
				.projectId(config.getProject().getId()).createdAt(config.getCreatedAt())
				.updatedAt(config.getUpdatedAt()).build();

		return ResponseEntity.ok(response);
	}

	// Update Config
	@PutMapping("/{configId}")
	public ResponseEntity<ProjectConfigResponseDTO> updateConfig(@PathVariable Long configId,
			@Valid @RequestBody ProjectConfigRequestDTO requestDTO, @RequestAttribute Long currentUserId,
			@RequestAttribute Role currentUserRole) {

		ProjectConfig updatedConfig = projectConfigService.updateConfig(configId, currentUserId, currentUserRole,
				requestDTO);

		ProjectConfigResponseDTO response = ProjectConfigResponseDTO.builder().id(updatedConfig.getId())
				.configName(updatedConfig.getConfigName()).configValue(updatedConfig.getConfigValue())
				.projectId(updatedConfig.getProject().getId()).createdAt(updatedConfig.getCreatedAt())
				.updatedAt(updatedConfig.getUpdatedAt()).build();

		return ResponseEntity.ok(response);
	}

	// Delete Config
	@DeleteMapping("/{configId}")
	public ResponseEntity<String> deleteConfig(@PathVariable Long configId, @RequestAttribute Long currentUserId,
			@RequestAttribute Role currentUserRole) {

		projectConfigService.deleteConfig(configId, currentUserId, currentUserRole);

		return ResponseEntity.ok("Project config deleted successfully");
	}
}
