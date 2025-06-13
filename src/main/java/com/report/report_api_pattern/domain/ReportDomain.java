package com.report.report_api_pattern.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class ReportDomain {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;
        private LocalDate createdAt;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

    }
