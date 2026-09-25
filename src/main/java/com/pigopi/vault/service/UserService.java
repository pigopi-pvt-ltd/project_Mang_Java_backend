package com.pigopi.vault.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pigopi.vault.dto.RegisterRequestDTO;
import com.pigopi.vault.dto.RemoveUserFromProjectResponseDTO;
import com.pigopi.vault.dto.UserUpdateRequestDTO;
import com.pigopi.vault.entity.Project;
//import com.pigopi.vault.entity.Role;
import com.pigopi.vault.entity.User;
import com.pigopi.vault.exception.ResourceAlreadyExistException;
import com.pigopi.vault.exception.ResourceNotFoundException;
import com.pigopi.vault.repository.ProjectRepository;
import com.pigopi.vault.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final ProjectRepository projectRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	// CREATE USER
	public User createUser(RegisterRequestDTO req) {
		if (userRepository.existsByEmail(req.getEmail())) {
			throw new ResourceAlreadyExistException("Email already Registered");
		}
		if (req.getOfficialEmail() != null && !req.getOfficialEmail().isBlank()) {
			if (userRepository.existsByOfficialEmail(req.getOfficialEmail())) {
				throw new ResourceAlreadyExistException("Official email already registered");
			}
		}
		User user = User.builder().username(req.getUsername()).password(passwordEncoder.encode(req.getPassword()))
				.email(req.getEmail()).role(req.getRole()).mobileNumber(req.getMobilenumber()).github(req.getGithub())
				.officialEmail(req.getOfficialEmail()).address(req.getAddress()).build();
		User saveUser = userRepository.save(user);
		emailService.sendAccountCreatedEmail(saveUser.getEmail(), saveUser.getUsername());
		return saveUser;
	}

	// GET ALL USERS
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	// GET USER BY ID
	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	// UPDATE USER
	public User updateUser(Long id, UserUpdateRequestDTO updateUser) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		// Check whether no field was provided
		if (updateUser.getUsername() == null && updateUser.getEmail() == null && updateUser.getMobilenumber() == null
				&& updateUser.getGithub() == null && updateUser.getOfficialEmail() == null
				&& updateUser.getAddress() == null) {

			throw new RuntimeException("Empty object not allowed!");
		}

		// Check blank values
		if (updateUser.getUsername() != null && updateUser.getUsername().isBlank()) {
			throw new RuntimeException("Username can't be blank");
		}

		if (updateUser.getEmail() != null && updateUser.getEmail().isBlank()) {
			throw new RuntimeException("Email can't be blank");
		}

		if (updateUser.getMobilenumber() != null && updateUser.getMobilenumber().isBlank()) {
			throw new RuntimeException("Mobile number can't be blank");
		}

		if (updateUser.getGithub() != null && updateUser.getGithub().isBlank()) {
			throw new RuntimeException("GitHub can't be blank");
		}

		if (updateUser.getOfficialEmail() != null && updateUser.getOfficialEmail().isBlank()) {
			throw new RuntimeException("Official email can't be blank");
		}

		if (updateUser.getAddress() != null && updateUser.getAddress().isBlank()) {
			throw new RuntimeException("Address can't be blank");
		}

		// Update only the fields that were provided

		if (updateUser.getUsername() != null) {
			user.setUsername(updateUser.getUsername().trim());
		}

		if (updateUser.getEmail() != null) {
			if (userRepository.existsByEmailAndIdNot(updateUser.getEmail(), id)) {
				throw new ResourceAlreadyExistException("Email already Registered");
			}
			user.setEmail(updateUser.getEmail().trim());
		}

		if (updateUser.getMobilenumber() != null) {
			user.setMobileNumber(updateUser.getMobilenumber());
		}

		if (updateUser.getGithub() != null) {
			user.setGithub(updateUser.getGithub().trim());
		}

		if (updateUser.getOfficialEmail() != null) {
			if (userRepository.existsByOfficialEmailAndIdNot(updateUser.getOfficialEmail(), id)) {
				throw new ResourceAlreadyExistException("Official-Email already Registered");
			}
			user.setOfficialEmail(updateUser.getOfficialEmail().trim());
		}

		if (updateUser.getAddress() != null) {
			user.setAddress(updateUser.getAddress().trim());
		}

		return userRepository.save(user);
	}

	// DELETE USER
	@Transactional
	public void deleteUser(Long id) {

		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		// Remove user from all assigned projects
		for (Project project : new HashSet<>(user.getProjects())) {
			user.removeProject(project);
		}
//		user.getProjects().clear();
		userRepository.delete(user);
	}

	// ASSIGN PROJECT
	@Transactional
	public User assignProject(Long userId, Long projectId) {

		User user = getUserById(userId);

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new RuntimeException("Project not found"));

		if (user.getProjects().contains(project)) {
			throw new ResourceAlreadyExistException("User is already assigned to this project");
		}
		user.addProject(project);
		User save = userRepository.save(user);
		emailService.sendProjectAssignedEmail(save.getEmail(), save.getUsername(), project.getName());
		return save;
	}

	// REMOVE User FROM PROJECT
	@Transactional
	public RemoveUserFromProjectResponseDTO removeUserFromProject(Long userId, Long projectId) {
		User user = getUserById(userId);
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

		if (!user.getProjects().contains(project)) {
			throw new ResourceNotFoundException("User is not assigned to this project");
		}
		String projectName = project.getName();
		user.removeProject(project);
		userRepository.save(user);
		return RemoveUserFromProjectResponseDTO.builder().userId(user.getId()).username(user.getUsername())
				.projectId(project.getId()).projectName(projectName).message("User removed from project successfully")
				.build();
	}

	// GET USER PROJECTS
	public User getUserAllProjects(Long userId) {
		User user = getUserById(userId);
		return user;
	}
}