package com.abhimanyu.jobportal.service.impl;

import com.abhimanyu.jobportal.dto.ApplicationRequestDTO;
import com.abhimanyu.jobportal.dto.ApplicationResponseDTO;
import com.abhimanyu.jobportal.entity.Application;
import com.abhimanyu.jobportal.entity.Job;
import com.abhimanyu.jobportal.entity.User;
import com.abhimanyu.jobportal.enums.ApplicationStatus;
import com.abhimanyu.jobportal.enums.Role;
import com.abhimanyu.jobportal.exception.ApplicationNotFoundException;
import com.abhimanyu.jobportal.exception.DuplicateApplicationException;
import com.abhimanyu.jobportal.exception.JobNotFoundException;
import com.abhimanyu.jobportal.exception.UserNotFoundException;
import com.abhimanyu.jobportal.repository.ApplicationRepository;
import com.abhimanyu.jobportal.repository.JobRepository;
import com.abhimanyu.jobportal.repository.UserRepository;
import com.abhimanyu.jobportal.service.ApplicationService;

import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    public ApplicationResponseDTO saveApplication(
            ApplicationRequestDTO dto) {

        User user = getCurrentUser();

        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() ->
                        new JobNotFoundException(
                                "Job not found with id: " + dto.getJobId()
                        )
                );

        if (applicationRepository.existsByUserIdAndJobId(
                user.getId(),
                job.getId())) {

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

    @Override
    public List<ApplicationResponseDTO> getAllApplications() {

        User user = getCurrentUser();

        if (user.getRole() == Role.ADMIN) {

            return applicationRepository.findAll()
                    .stream()
                    .map(this::convertToResponseDTO)
                    .toList();
        }

        if (user.getRole() == Role.CANDIDATE) {

            return applicationRepository
                    .findByUserId(user.getId())
                    .stream()
                    .map(this::convertToResponseDTO)
                    .toList();
        }

        if (user.getRole() == Role.RECRUITER) {

            return applicationRepository
                    .findByJobPostedById(user.getId())
                    .stream()
                    .map(this::convertToResponseDTO)
                    .toList();
        }

        return List.of();
    }

    @Override
    public ApplicationResponseDTO getApplicationById(Long id) {

        Application application =
                applicationRepository.findById(id)
                        .orElseThrow(() ->
                                new ApplicationNotFoundException(
                                        "Application not found with id: " + id
                                )
                        );

        validateApplicationAccess(application);

        return convertToResponseDTO(application);
    }

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

        User user = getCurrentUser();

        if (user.getRole() != Role.ADMIN
                && (application.getJob() == null
                || application.getJob().getPostedBy() == null
                || !application.getJob()
                        .getPostedBy()
                        .getId()
                        .equals(user.getId()))) {

            throw new RuntimeException(
                    "You are not allowed to update this application"
            );
        }

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

    @Override
    public void deleteApplication(Long id) {

        Application application =
                applicationRepository.findById(id)
                        .orElseThrow(() ->
                                new ApplicationNotFoundException(
                                        "Application not found with id: " + id
                                )
                        );

        User user = getCurrentUser();

        if (user.getRole() != Role.ADMIN
                && (application.getUser() == null
                || !application.getUser()
                        .getId()
                        .equals(user.getId()))) {

            throw new RuntimeException(
                    "You are not allowed to delete this application"
            );
        }

        applicationRepository.delete(application);
    }

    private User getCurrentUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: " + email
                        )
                );
    }

    private void validateApplicationAccess(
            Application application) {

        User user = getCurrentUser();

        if (user.getRole() == Role.ADMIN) {
            return;
        }

        if (user.getRole() == Role.CANDIDATE) {

            if (application.getUser() == null
                    || !application.getUser()
                    .getId()
                    .equals(user.getId())) {

                throw new RuntimeException(
                        "You are not allowed to access this application"
                );
            }

            return;
        }

        if (user.getRole() == Role.RECRUITER) {

            if (application.getJob() == null
                    || application.getJob().getPostedBy() == null
                    || !application.getJob()
                    .getPostedBy()
                    .getId()
                    .equals(user.getId())) {

                throw new RuntimeException(
                        "You are not allowed to access this application"
                );
            }
        }
    }

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