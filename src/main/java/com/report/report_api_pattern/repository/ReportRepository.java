package com.report.report_api_pattern.repository;

import com.report.report_api_pattern.domain.ReportDomain;
import com.report.report_api_pattern.dto.ReportWithUserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportDomain, Long> {

    @Query("SELECT new com.report.report_api_pattern.dto.ReportWithUserDTO(r.title, r.createdAt, u.name) " +
            "FROM ReportDomain r JOIN r.user u")
    List<ReportWithUserDTO> findAllReportsWithUser();

}
