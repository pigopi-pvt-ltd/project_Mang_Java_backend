package com.pigopi.vault.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pigopi.vault.dto.ProjectRequestDTO;
import com.pigopi.vault.dto.ProjectResponseDTO;
import com.pigopi.vault.dto.ProjectUpdateRequestDTO;
import com.pigopi.vault.entity.Project;
import com.pigopi.vault.entity.Role;
import com.pigopi.vault.exception.UnAuthorizedException;
import com.pigopi.vault.service.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;

	// CREATE PROJECT
	@PostMapping
	public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectRequestDTO projectdto) {
		Project project = projectService.createProject(projectdto);
		ProjectResponseDTO responseDTO = ProjectResponseDTO.builder().id(project.getId()).name(project.getName())
				.description(project.getDescription()).build();
		return ResponseEntity.ok(responseDTO);
	}

	// GET ALL PROJECTS
	@GetMapping
	public ResponseEntity<List<Project>> getAllProjects(@RequestAttribute Role currentUserRole) {
		if (currentUserRole != Role.SuperAdmin && currentUserRole != Role.Manager) {
			throw new UnAuthorizedException("Only SuperAdmin Or Manager view All Projects");
		}
		List<Project> allProjects = projectService.getAllProjects();
		return ResponseEntity.ok(allProjects);
	}

	// GET PROJECT BY ID
	@GetMapping("/{id}")
	public ResponseEntity<Project> getProject(@PathVariable Long id) {
		return ResponseEntity.ok(projectService.getProjectById(id));
	}

	// GET PROJECT BY NAME
	@GetMapping("/name/{name}")
	public ResponseEntity<Project> getProjectByName(@PathVariable String name) {

		return ResponseEntity.ok(projectService.getProjectByName(name));
	}

	// UPDATE PROJECT
	@PutMapping("/{id}")
	public ResponseEntity<Project> updateProject(@PathVariable Long id,
			@Valid @RequestBody ProjectUpdateRequestDTO projectdto, @RequestAttribute Role currentUserRole) {
		if (currentUserRole != Role.SuperAdmin && currentUserRole != Role.Manager) {
			throw new UnAuthorizedException("Only SuperAdmin and Manager can update projects");
		}
		Project project = Project.builder().name(projectdto.getName()).description(projectdto.getDescription()).build();
		Project updatedProject = projectService.updateProject(id, project);
		return ResponseEntity.ok(updatedProject);
	}

	// DELETE PROJECT
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProject(@PathVariable Long id) {

		projectService.deleteProject(id);

		return ResponseEntity.ok("Project deleted successfully");
	}
//current user All projects
	@GetMapping("/me/projects")
	public ResponseEntity<List<ProjectResponseDTO>> getMyProjects(@RequestAttribute Long currentUserId) {

		List<Project> projects = projectService.getMyProjects(currentUserId);
		List<ProjectResponseDTO> resplist = projects.stream()
        .map(project -> ProjectResponseDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .build())
        .toList();
		return ResponseEntity.ok(resplist);
	}

}