package zatribune.spring.jasperreports.db.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReportImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String relativePath;

    public ReportImage(String name, String relativePath) {
        this.name = name;
        this.relativePath = relativePath;
    }

    @ManyToMany(mappedBy = "images")
    private Set<Report> reports=new HashSet<>(1);
}
