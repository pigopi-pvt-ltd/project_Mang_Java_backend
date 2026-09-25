
package com.pigopi.vault.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectConfigRequestDTO {

    @NotBlank(message = "Config name is required")
    @Size(max = 100, message = "Config name cannot exceed 100 characters")
    private String configName;

    @Size(max = 5000, message = "Config value cannot exceed 5000 characters")
    private String configValue;
}

