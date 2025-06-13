package com.report.report_api_pattern.repository;

import com.report.report_api_pattern.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByNameContainingIgnoreCase(String namePart);


}