package zatribune.spring.jasperreports.db.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ReportField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nameEn;
    private String nameAr;

    @ManyToOne
    private Report report;


    public ReportField(String name) {
        this.name = name;
    }

    public ReportField(String name, String nameEn, String nameAr) {
        this.name = name;
        this.nameEn = nameEn;
        this.nameAr = nameAr;
    }
}
