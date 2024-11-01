package com.tribune.demo.reporting.db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Table(name = "REPORT_FIELD")
@Entity
public class ReportField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report", referencedColumnName = "id")
    private Report report;


    public ReportField(String name) {
        this.name = name;
    }

}
