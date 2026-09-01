package com.abhimanyu.jobportal.controller;

import com.abhimanyu.jobportal.dto.ApplicationRequestDTO;
import com.abhimanyu.jobportal.dto.ApplicationResponseDTO;
import com.abhimanyu.jobportal.service.ApplicationService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public List<ApplicationResponseDTO> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @PostMapping
    public ApplicationResponseDTO createApplication(
            @Valid @RequestBody ApplicationRequestDTO dto) {
        return applicationService.saveApplication(dto);
    }

    @GetMapping("/{id}")
    public ApplicationResponseDTO getApplicationById(
            @PathVariable Long id) {
        return applicationService.getApplicationById(id);
    }

    @PutMapping("/{id}/status")
    public ApplicationResponseDTO updateApplicationStatus(
            @PathVariable Long id,
            @RequestBody String status) {
        return applicationService.updateApplicationStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
    }
}