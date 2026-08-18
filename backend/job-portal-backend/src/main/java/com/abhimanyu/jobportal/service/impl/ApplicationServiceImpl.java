package com.abhimanyu.jobportal.service.impl;

import com.abhimanyu.jobportal.dto.ApplicationRequestDTO;
import com.abhimanyu.jobportal.dto.ApplicationResponseDTO;
import com.abhimanyu.jobportal.entity.Application;
import com.abhimanyu.jobportal.entity.Job;
import com.abhimanyu.jobportal.entity.User;
import com.abhimanyu.jobportal.enums.ApplicationStatus;
import com.abhimanyu.jobportal.exception.ApplicationNotFoundException;
import com.abhimanyu.jobportal.exception.DuplicateApplicationException;
import com.abhimanyu.jobportal.exception.JobNotFoundException;
import com.abhimanyu.jobportal.exception.UserNotFoundException;
import com.abhimanyu.jobportal.repository.ApplicationRepository;
import com.abhimanyu.jobportal.repository.JobRepository;
import com.abhimanyu.jobportal.repository.UserRepository;
import com.abhimanyu.jobportal.service.ApplicationService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ApplicationServiceImpl(
            ApplicationRepository applicationRepository,
            UserRepository userRepository,
            JobRepository jobRepository) {

        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    // =========================
    // CREATE APPLICATION
    // =========================

    @Override
    public ApplicationResponseDTO saveApplication(
            ApplicationRequestDTO dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + dto.getUserId()
                        )
                );

        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() ->
                        new JobNotFoundException(
                                "Job not found with id: " + dto.getJobId()
                        )
                );

        // Check duplicate application
        if (applicationRepository.existsByUserIdAndJobId(
                dto.getUserId(),
                dto.getJobId())) {

            throw new DuplicateApplicationException(
                    "You have already applied for this job"
            );
        }

        Application application = new Application();

        application.setUser(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedAt(LocalDateTime.now());

        Application savedApplication =
                applicationRepository.save(application);

        return convertToResponseDTO(savedApplication);
    }

    // =========================
    // GET ALL APPLICATIONS
    // =========================

    @Override
    public List<ApplicationResponseDTO> getAllApplications() {

        return applicationRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // =========================
    // GET APPLICATION BY ID
    // =========================

    @Override
    public ApplicationResponseDTO getApplicationById(Long id) {

        Application application =
                applicationRepository.findById(id)
                        .orElseThrow(() ->
                                new ApplicationNotFoundException(
                                        "Application not found with id: " + id
                                )
                        );

        return convertToResponseDTO(application);
    }

    // =========================
    // UPDATE APPLICATION STATUS
    // =========================

    @Override
    public ApplicationResponseDTO updateApplicationStatus(
            Long id,
            String status) {

        Application application =
                applicationRepository.findById(id)
                        .orElseThrow(() ->
                                new ApplicationNotFoundException(
                                        "Application not found with id: " + id
                                )
                        );

        ApplicationStatus applicationStatus;

        try {

            applicationStatus =
                    ApplicationStatus.valueOf(
                            status.trim().toUpperCase()
                    );

        } catch (IllegalArgumentException ex) {

            throw new RuntimeException(
                    "Invalid application status: " + status
            );
        }

        application.setStatus(applicationStatus);

        Application updatedApplication =
                applicationRepository.save(application);

        return convertToResponseDTO(updatedApplication);
    }

    // =========================
    // DELETE APPLICATION
    // =========================

    @Override
    public void deleteApplication(Long id) {

        Application application =
                applicationRepository.findById(id)
                        .orElseThrow(() ->
                                new ApplicationNotFoundException(
                                        "Application not found with id: " + id
                                )
                        );

        applicationRepository.delete(application);
    }

    // =========================
    // ENTITY -> RESPONSE DTO
    // =========================

    private ApplicationResponseDTO convertToResponseDTO(
            Application application) {

        ApplicationResponseDTO response =
                new ApplicationResponseDTO();

        response.setId(application.getId());

        if (application.getUser() != null) {
            response.setUserId(
                    application.getUser().getId()
            );
        }

        if (application.getJob() != null) {
            response.setJobId(
                    application.getJob().getId()
            );
        }

        response.setStatus(
                application.getStatus() != null
                        ? application.getStatus().name()
                        : null
        );

        response.setAppliedAt(
                application.getAppliedAt()
        );

        return response;
    }
}