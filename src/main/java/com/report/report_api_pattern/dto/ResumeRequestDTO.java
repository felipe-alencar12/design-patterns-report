package com.report.report_api_pattern.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResumeRequestDTO {
    private String name;
    private String email;
    private String phone;
    private List<ExperienceDTO> experiences;
    private List<String> skills;
}

