package com.abhimanyu.jobportal.service;

import com.abhimanyu.jobportal.dto.JobRequestDTO;
import com.abhimanyu.jobportal.dto.JobResponseDTO;

import java.util.List;

public interface JobService {

    JobResponseDTO saveJob(JobRequestDTO jobRequestDTO);

    List<JobResponseDTO> getAllJobs();

    JobResponseDTO getJobById(Long id);

    JobResponseDTO updateJob(Long id, JobRequestDTO jobRequestDTO);

    void deleteJob(Long id);
}