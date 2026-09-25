package com.pigopi.vault.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pigopi.vault.dto.ProjectRequestDTO;
import com.pigopi.vault.entity.Project;
import com.pigopi.vault.entity.User;
import com.pigopi.vault.exception.ResourceNotFoundException;
import com.pigopi.vault.repository.ProjectRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

	private final ProjectRepository projectRepository;

	// CREATE
	public Project createProject(ProjectRequestDTO projectdto) {
		Project project = Project.builder().name(projectdto.getName()).description(projectdto.getDescription()).build();
		return projectRepository.save(project);
	}

	// GET ALL PROJECTS
	public List<Project> getAllProjects() {
		return projectRepository.findAll();
	}

	// GET BY ID
	public Project getProjectById(Long id) {
		return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
	}

	// GET BY NAME
	public Project getProjectByName(String name) {

		return projectRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
	}

	// UPDATE
	public Project updateProject(Long id, Project updatedProject) {

		Project project = getProjectById(id);
		if (updatedProject.getName() == null && updatedProject.getDescription() == null) {
			throw new ResourceNotFoundException("Atleast One Field is Required");
		}
		// Update name only if provided
		if (updatedProject.getName() != null) {
			if (updatedProject.getName().isBlank()) {
				throw new ResourceNotFoundException("Project name cannot be blank");
			}
			project.setName(updatedProject.getName().trim());
		}

		if (updatedProject.getDescription() != null) {
			if (updatedProject.getDescription().isBlank()) {
				throw new ResourceNotFoundException("Description cannot be blank");
			}
			project.setDescription(updatedProject.getDescription().trim());
		}
		return projectRepository.save(project);
	}

	// DELETE
	@Transactional
	public void deleteProject(Long id) {

		Project project = getProjectById(id);
		// Remove project from all assigned users
	    for (User user : new HashSet<>(project.getUsers())) {
	        user.removeProject(project);
	    }
		projectRepository.delete(project);
	}
	//current user projects
	  public List<Project> getMyProjects(Long userId) {

	        List<Project> projects =
	                projectRepository.findByUsers_Id(userId);

	        return projects;
	    }
}