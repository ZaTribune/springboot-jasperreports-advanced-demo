package com.tribune.demo.reporting.db.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "REPORT_TABLE")
@Entity
public class ReportTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //name of the property on json or pdfRequest
    private String name;

    private String description;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "reportTable")
    private Set<ReportTableColumn> tableColumns = new HashSet<>(1);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report", referencedColumnName = "id")
    private Report report;

    public ReportTable(String name) {
        this.name = name;
    }

    public ReportTable(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public void addListField(String listFieldName) {
        ReportTableColumn field = new ReportTableColumn(listFieldName);
        field.setReportTable(this);
        tableColumns.add(field);
    }

}
