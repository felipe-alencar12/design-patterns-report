package com.report.report_api_pattern.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReportWithUserDTO {
    private String reportTitle;
    private LocalDate reportDate;
    private String userName;

    public ReportWithUserDTO(String reportTitle, LocalDate reportDate, String userName) {
        this.reportTitle = reportTitle;
        this.reportDate = reportDate;
        this.userName = userName;
    }

}
