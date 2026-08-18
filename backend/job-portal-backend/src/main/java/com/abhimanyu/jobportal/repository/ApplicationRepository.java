package com.abhimanyu.jobportal.repository;

import com.abhimanyu.jobportal.entity.Application;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    boolean existsByUserIdAndJobId(Long userId, Long jobId);

}
