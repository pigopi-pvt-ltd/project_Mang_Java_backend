package com.pigopi.vault.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
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

import com.pigopi.vault.dto.AssignNewProjectToUserResponseDTO;
import com.pigopi.vault.dto.ProjectResponseDTO;
import com.pigopi.vault.dto.RegisterRequestDTO;
import com.pigopi.vault.dto.RegisterResponseDTO;
import com.pigopi.vault.dto.RemoveUserFromProjectResponseDTO;
import com.pigopi.vault.dto.UserAllProjectsResponseDTO;
import com.pigopi.vault.dto.UserResponseDTO;
import com.pigopi.vault.dto.UserUpdateRequestDTO;
import com.pigopi.vault.entity.Project;
import com.pigopi.vault.entity.Role;
import com.pigopi.vault.entity.User;
import com.pigopi.vault.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// CREATE EMPLOYEE / MANAGER
	@PostMapping
	public ResponseEntity<RegisterResponseDTO> createUser(@Valid @RequestBody RegisterRequestDTO userReqDTO) {

		User saveUser = userService.createUser(userReqDTO);
		RegisterResponseDTO respDTO = RegisterResponseDTO.builder().id(saveUser.getId())
				.username(saveUser.getUsername()).email(saveUser.getEmail()).role(saveUser.getRole())
				.message("User Registerd Successfully ! ").build();
		return new ResponseEntity<RegisterResponseDTO>(respDTO, HttpStatus.CREATED);
	}

	// GET ALL USERS
	@GetMapping
	public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
		List<User> allUsers = userService.getAllUsers();
		List<UserResponseDTO> respList = new ArrayList<>();
		for (User x : allUsers) {
			UserResponseDTO respDTO = UserResponseDTO.builder().id(x.getId()).username(x.getUsername())
					.email(x.getEmail()).role(x.getRole()).mobileNumber(x.getMobileNumber()).github(x.getGithub())
					.address(x.getAddress()).officialEmail(x.getOfficialEmail())
					.message(x.getUsername() + " ( " + x.getId() + " ) Details").build();
			respList.add(respDTO);

		}
		return ResponseEntity.ok(respList);
	}

	// GET USER
	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
		User userById = userService.getUserById(id);
		UserResponseDTO userByIdDto = UserResponseDTO.builder().id(userById.getId()).username(userById.getUsername())
				.email(userById.getEmail()).role(userById.getRole()).mobileNumber(userById.getMobileNumber())
				.github(userById.getGithub()).officialEmail(userById.getOfficialEmail()).address(userById.getAddress())
				.build();
		return ResponseEntity.ok(userByIdDto);
	}

	// UPDATE USER
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequestDTO updateReqDTO,
			@RequestAttribute Long currentUserId, @RequestAttribute Role currentUserRole) {
		// reqAttribute set in intercepter class
		if (!id.equals(currentUserId) && currentUserRole != Role.SuperAdmin) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\":\"You can update only your profile\"}");
		}

		User updateUser = userService.updateUser(id, updateReqDTO);
		UserResponseDTO updateDto = UserResponseDTO.builder().id(updateUser.getId()).username(updateUser.getUsername())
				.email(updateUser.getEmail()).address(updateUser.getAddress()).role(updateUser.getRole())
				.github(updateUser.getGithub()).officialEmail(updateUser.getOfficialEmail())
				.mobileNumber(updateUser.getMobileNumber()).message("User Updated").build();
		return ResponseEntity.ok(updateDto);
	}

	// DELETE USER
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {

		userService.deleteUser(id);
		return ResponseEntity.ok("User deleted successfully");
	}

	// ASSIGN PROJECT
	@PostMapping("/{userId}/projects/{projectId}")
	public ResponseEntity<AssignNewProjectToUserResponseDTO> assignProject(@PathVariable Long userId,
			@PathVariable Long projectId) {
		User assignProjToUser = userService.assignProject(userId, projectId);

		Project project = assignProjToUser.getProjects().stream().filter(p -> p.getId().equals(projectId)).findFirst()
				.orElseThrow();

		AssignNewProjectToUserResponseDTO assignProDto = AssignNewProjectToUserResponseDTO.builder()
				.id(assignProjToUser.getId()).email(assignProjToUser.getEmail())
				.username(assignProjToUser.getUsername())
				.message("Project Assigned to :" + assignProjToUser.getUsername()).project(ProjectResponseDTO.builder()
						.id(project.getId()).name(project.getName()).description(project.getDescription()).build())
				.build();
		return ResponseEntity.ok(assignProDto);
	}

	// REMOVE User From PROJECT
	@DeleteMapping("/{userId}/projects/{projectId}")
	public ResponseEntity<RemoveUserFromProjectResponseDTO> removeUserFromProject(@PathVariable Long userId,
			@PathVariable Long projectId) {
		RemoveUserFromProjectResponseDTO resp = userService.removeUserFromProject(userId, projectId);
		return ResponseEntity.ok(resp);
	}

	// GET USER'S ALL PROJECTS For ADMIN OR MANAGER
	@GetMapping("/{userId}/projects")
	public ResponseEntity<UserAllProjectsResponseDTO> getUserAllProjects(@PathVariable Long userId) {

		User user = userService.getUserAllProjects(userId);

		Set<ProjectResponseDTO> projects = user
				.getProjects().stream().map(project -> ProjectResponseDTO.builder().id(project.getId())
						.name(project.getName()).description(project.getDescription()).build())
				.collect(Collectors.toSet());

		UserAllProjectsResponseDTO response = UserAllProjectsResponseDTO.builder().userId(user.getId())
				.username(user.getUsername()).projects(projects).message("All Projects").build();

		return ResponseEntity.ok(response);
	}
}