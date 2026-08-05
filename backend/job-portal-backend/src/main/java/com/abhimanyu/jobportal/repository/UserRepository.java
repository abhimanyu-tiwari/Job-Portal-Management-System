package com.abhimanyu.jobportal.repository;

import com.abhimanyu.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}