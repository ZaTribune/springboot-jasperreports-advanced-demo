package zatribune.spring.jasperreports.db.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameEn;
    private String nameAr;
    private String templateEn;
    private String templateAr;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "report")
    private Set<ReportField> reportFields = new HashSet<>(1);

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "report")
    private Set<ReportList> reportLists = new HashSet<>(1);

    public Report(String nameEn) {
        this.nameEn = nameEn;
    }

    public void addReportField(String fieldName, String nameEn, String nameAr) {
        ReportField field = new ReportField(fieldName, nameEn, nameAr);
        field.setReport(this);
        reportFields.add(field);
    }

    public void addReportList(ReportList list) {
        list.setReport(this);
        reportLists.add(list);
    }

}