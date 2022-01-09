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
public class ReportList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //name of the property on json or pdfRequest
    private String name;
    //title of the property to be displayed on the template helps on headers...etc
    private String nameEn;
    private String nameAr;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "reportList")
    private Set<ReportListField> listFields = new HashSet<>(1);

    @ManyToOne
    private Report report;

    public ReportList(String name) {
        this.name = name;
    }

    public ReportList(String name, String nameEn, String nameAr) {
        this.name = name;
        this.nameEn = nameEn;
        this.nameAr = nameAr;
    }


    public void addListField(String listFieldName) {
        ReportListField field = new ReportListField(listFieldName);
        field.setReportList(this);
        listFields.add(field);
    }

    public void addListField(String listFieldName, String nameEn, String nameAr) {
        ReportListField field = new ReportListField(listFieldName, nameEn, nameAr);
        field.setReportList(this);
        listFields.add(field);
    }


}
