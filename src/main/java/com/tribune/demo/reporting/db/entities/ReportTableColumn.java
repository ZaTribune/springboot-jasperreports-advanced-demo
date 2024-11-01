package com.tribune.demo.reporting.db.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "REPORT_TABLE_COLUMN")
@Entity
public class ReportTableColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //name of the property on json or pdfRequest
    private String name;

    private String description;

    public ReportTableColumn(String name) {
        this.name = name;
    }

    @ManyToOne
    private ReportTable reportTable;
}
