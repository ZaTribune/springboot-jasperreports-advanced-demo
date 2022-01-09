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
public class ReportLocale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    private String templatePath;

    @ManyToMany(mappedBy = "locales")
    private Set<Report> reports=new HashSet<>(1);


    public ReportLocale(String value, String templatePath) {
        this.value = value;
        this.templatePath = templatePath;
    }
}
