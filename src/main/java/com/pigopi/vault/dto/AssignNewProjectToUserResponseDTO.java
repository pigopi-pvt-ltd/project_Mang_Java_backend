package com.pigopi.vault.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignNewProjectToUserResponseDTO {

    private Long id;

    private String username;

    private String email;

    private ProjectResponseDTO project;

    private String message;
}