package zatribune.spring.jasperreports.db.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import zatribune.spring.jasperreports.db.entities.Report;


import java.util.Optional;

public interface ReportRepository extends CrudRepository<Report, Long> {


    @Query("select r from Report r left join fetch r.reportFields left join fetch r.reportLists" +
            " where r.id=?1")
    Optional<Report> findById(Long id);

    @Query("select r from Report r left join fetch r.reportFields left join fetch r.reportLists" +
            " where lower(r.name) =lower(?1)")
    Optional<Report> findByName(String name);
}
