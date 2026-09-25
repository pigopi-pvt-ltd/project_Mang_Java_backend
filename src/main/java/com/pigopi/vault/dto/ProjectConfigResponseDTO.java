package com.pigopi.vault.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectConfigResponseDTO {

    private Long id;

    private String configName;

    private String configValue;

    private Long projectId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

