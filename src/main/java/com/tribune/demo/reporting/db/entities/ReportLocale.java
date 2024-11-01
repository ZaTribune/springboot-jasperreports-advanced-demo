package com.tribune.demo.reporting.db.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "REPORT_LOCALE")
@Entity
public class ReportLocale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String templatePath;

    @ManyToMany(mappedBy = "locales")
    private Set<Report> reports = new HashSet<>(1);


    public ReportLocale(String content, String templatePath) {
        this.content = content;
        this.templatePath = templatePath;
    }
}
