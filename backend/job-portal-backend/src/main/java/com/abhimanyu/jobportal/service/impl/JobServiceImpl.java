package com.abhimanyu.jobportal.service.impl;

import com.abhimanyu.jobportal.dto.JobRequestDTO;
import com.abhimanyu.jobportal.dto.JobResponseDTO;
import com.abhimanyu.jobportal.entity.Job;
import com.abhimanyu.jobportal.entity.User;
import com.abhimanyu.jobportal.exception.JobNotFoundException;
import com.abhimanyu.jobportal.exception.UserNotFoundException;
import com.abhimanyu.jobportal.repository.JobRepository;
import com.abhimanyu.jobportal.repository.UserRepository;
import com.abhimanyu.jobportal.service.JobService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobServiceImpl(
            JobRepository jobRepository,
            UserRepository userRepository) {

        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @Override
    public JobResponseDTO saveJob(JobRequestDTO dto) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: " + email
                        )
                );

        Job job = new Job();

        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setCompanyName(dto.getCompanyName());
        job.setLocation(dto.getLocation());
        job.setSalary(dto.getSalary());
        job.setJobType(dto.getJobType());
        job.setSkills(dto.getSkills());
        job.setPostedBy(user);

        Job savedJob = jobRepository.save(job);

        return convertToResponseDTO(savedJob);
    }

    @Override
    public List<JobResponseDTO> getAllJobs() {

        return jobRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    public JobResponseDTO getJobById(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new JobNotFoundException(
                                "Job not found with id: " + id
                        )
                );

        return convertToResponseDTO(job);
    }

    @Override
    public JobResponseDTO updateJob(
            Long id,
            JobRequestDTO dto) {

        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() ->
                        new JobNotFoundException(
                                "Job not found with id: " + id
                        )
                );

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: " + email
                        )
                );

        if (existingJob.getPostedBy() == null
                || !existingJob.getPostedBy()
                        .getId()
                        .equals(user.getId())) {

            throw new RuntimeException(
                    "You are not allowed to update this job"
            );
        }

        existingJob.setTitle(dto.getTitle());
        existingJob.setDescription(dto.getDescription());
        existingJob.setCompanyName(dto.getCompanyName());
        existingJob.setLocation(dto.getLocation());
        existingJob.setSalary(dto.getSalary());
        existingJob.setJobType(dto.getJobType());
        existingJob.setSkills(dto.getSkills());

        Job updatedJob = jobRepository.save(existingJob);

        return convertToResponseDTO(updatedJob);
    }

    @Override
    public void deleteJob(Long id) {

        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() ->
                        new JobNotFoundException(
                                "Job not found with id: " + id
                        )
                );

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: " + email
                        )
                );

        if (existingJob.getPostedBy() == null
                || !existingJob.getPostedBy()
                        .getId()
                        .equals(user.getId())) {

            throw new RuntimeException(
                    "You are not allowed to delete this job"
            );
        }

        jobRepository.delete(existingJob);
    }

    private JobResponseDTO convertToResponseDTO(Job job) {

        JobResponseDTO response = new JobResponseDTO();

        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setCompanyName(job.getCompanyName());
        response.setLocation(job.getLocation());
        response.setSalary(job.getSalary());
        response.setJobType(job.getJobType());
        response.setSkills(job.getSkills());

        if (job.getPostedBy() != null) {
            response.setUserId(
                    job.getPostedBy().getId()
            );
        }

        return response;
    }
}