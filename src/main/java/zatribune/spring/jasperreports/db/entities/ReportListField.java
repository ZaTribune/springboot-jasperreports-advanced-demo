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

    private String description;

    public ReportListField(String name) {
        this.name = name;
    }

    @ManyToOne
    private ReportList reportList;
}
