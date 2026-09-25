package com.pigopi.vault.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pigopi.vault.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

	Optional<Project> findByName(String name);

	List<Project> findByUsers_Id(Long userId);

	boolean existsByIdAndUsers_Id(Long projectId, Long userId);
}
