package com.pigopi.vault;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.pigopi.vault.entity.Role;
import com.pigopi.vault.entity.User;
import com.pigopi.vault.repository.UserRepository;

@SpringBootApplication
public class PigoPiVaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigoPiVaultApplication.class, args);
	}
	@Bean
	CommandLineRunner createSuperAdmin(UserRepository userRepository) {

	    return new CommandLineRunner() {

	        @Override
	        public void run(String... args) {

	            if (userRepository.findByEmail("admin@pigopi.com").isEmpty()) {

	                User admin = User.builder()
	                        .username("Super Admin")
	                        .email("admin@pigopi.com")
	                        .password("admin123")
	                        .role(Role.SuperAdmin).mobileNumber("8006004005")
	                        .build();

	                userRepository.save(admin);
	            }
	        }
	    };
	}
}
