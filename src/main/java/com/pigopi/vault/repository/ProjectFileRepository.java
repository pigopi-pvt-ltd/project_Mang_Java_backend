package com.pigopi.vault.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pigopi.vault.entity.ProjectFile;

public interface ProjectFileRepository
        extends JpaRepository<ProjectFile, Long> {
    List<ProjectFile> findByProjectId(Long projectId);
}