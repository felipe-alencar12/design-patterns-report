package com.report.report_api_pattern.controller;

import com.report.report_api_pattern.domain.ReportDomain;
import com.report.report_api_pattern.dto.ReportWithUserDTO;
import com.report.report_api_pattern.repository.ReportRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportRepository repository;

    public ReportController(ReportRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<ReportDomain> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public ReportDomain create(@RequestBody ReportDomain report) {
        return repository.save(report);
    }

    @GetMapping("/with-user")
    public List<ReportWithUserDTO> getReportsWithUser() {
        return repository.findAllReportsWithUser();
    }
}

