package com.tribune.demo.reporting.db.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
@Table(name = "REPORT")
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "report")
    private Set<ReportField> reportFields = new HashSet<>(1);

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "report")
    private Set<ReportTable> reportTables = new HashSet<>(1);

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "REPORT_IMAGE_LINKER",
            joinColumns = @JoinColumn(name = "report"),
            inverseJoinColumns = @JoinColumn(name = "image"))
    private Set<ReportImage> images = new HashSet<>(1);

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "REPORT_LOCALE_LINKER",
            joinColumns = @JoinColumn(name = "report"),
            inverseJoinColumns = @JoinColumn(name = "locale"))
    private Set<ReportLocale> locales = new HashSet<>(1);

    public Report(String name) {
        this.name = name;
    }

    public void addReportField(String fieldName) {
        ReportField field = new ReportField(fieldName);
        field.setReport(this);
        reportFields.add(field);
    }

    public void addReportTable(ReportTable list) {
        list.setReport(this);
        reportTables.add(list);
    }

    public void addReportImage(ReportImage image) {
        image.getReports().add(this);
        images.add(image);
    }

    public void addReportLocale(ReportLocale locale) {
        locale.getReports().add(this);
        locales.add(locale);
    }

    public List<String> getReportLocalesValues() {
        return locales.stream().map(ReportLocale::getContent).collect(Collectors.toList());
    }

}