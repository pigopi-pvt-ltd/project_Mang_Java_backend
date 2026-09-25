
package com.pigopi.vault.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectFileUploadRequestDTO {

    private MultipartFile file;
}

