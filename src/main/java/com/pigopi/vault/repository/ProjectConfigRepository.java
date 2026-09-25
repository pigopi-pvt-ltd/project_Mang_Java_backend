package com.pigopi.vault.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pigopi.vault.entity.ProjectConfig;

public interface ProjectConfigRepository extends JpaRepository<ProjectConfig, Long> {

	List<ProjectConfig> findByProjectId(Long projectId);

	boolean existsByProjectIdAndConfigName(Long projectId, String configName);

	boolean existsByProjectIdAndConfigNameAndIdNot(Long projectId, String configName, Long id);
}