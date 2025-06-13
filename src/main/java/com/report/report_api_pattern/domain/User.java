package com.report.report_api_pattern.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Relacionamento com ReportDomain
    @OneToMany(mappedBy = "user")
    private List<ReportDomain> reports;
}
