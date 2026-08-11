package com.abhimanyu.jobportal.service;

import com.abhimanyu.jobportal.dto.ApplicationRequestDTO;
import com.abhimanyu.jobportal.dto.ApplicationResponseDTO;

import java.util.List;

public interface ApplicationService {

    ApplicationResponseDTO saveApplication(
            ApplicationRequestDTO applicationRequestDTO);

    List<ApplicationResponseDTO> getAllApplications();

    ApplicationResponseDTO getApplicationById(Long id);

    ApplicationResponseDTO updateApplicationStatus(
            Long id,
            String status);

    void deleteApplication(Long id);
}