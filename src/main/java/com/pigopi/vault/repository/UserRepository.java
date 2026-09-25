package com.pigopi.vault.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pigopi.vault.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByOfficialEmail(String officialEmail);

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	boolean existsByOfficialEmailAndIdNot(String officialEmail, Long id);

	boolean existsByOfficialEmail(String officialEmail);

	boolean existsByEmailAndIdNot(String email, Long id);
}
