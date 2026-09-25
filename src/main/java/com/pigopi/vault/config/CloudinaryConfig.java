
package com.pigopi.vault.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

	@Value("${cloudinary.cloud-name}")
	private String cloudName;

	@Value("${cloudinary.api-key}")
	private String apiKey;

	@Value("${cloudinary.api-secret}")
	private String apiSecret;

	@Bean
	Cloudinary cloudinary() {

		Cloudinary cloudinary = new Cloudinary();
		cloudinary.config.cloudName = cloudName;
		cloudinary.config.apiKey = apiKey;
		cloudinary.config.apiSecret = apiSecret;
		return cloudinary;
	}
}