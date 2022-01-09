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

    private String description;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "reportList")
    private Set<ReportListField> listFields = new HashSet<>(1);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report",referencedColumnName = "id")
    private Report report;

    public ReportList(String name) {
        this.name = name;
    }

    public ReportList(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public void addListField(String listFieldName) {
        ReportListField field = new ReportListField(listFieldName);
        field.setReportList(this);
        listFields.add(field);
    }

}
