package zatribune.spring.jasperreports.db.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ReportListField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //name of the property on json or pdfRequest
    private String name;

    //title of the property to be displayed on the template helps on headers...etc
    private String nameEn;
    private String nameAr;

    public ReportListField(String name) {
        this.name = name;
    }

    public ReportListField(String name, String nameEn, String nameAr) {
        this.name = name;
        this.nameEn = nameEn;
        this.nameAr = nameAr;
    }

    @ManyToOne
    private ReportList reportList;
}
