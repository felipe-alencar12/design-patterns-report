package com.report.report_api_pattern.dto;

import lombok.Data;

@Data
public class ExperienceDTO {
    private String company;
    private String role;
    private String startDate;
    private String endDate;
    private boolean currentJob;
    private String description;
}

