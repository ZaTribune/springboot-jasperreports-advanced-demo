package com.tribune.demo.reporting.db.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import com.tribune.demo.reporting.db.entities.Report;


import java.util.Optional;

public interface ReportRepository extends CrudRepository<Report, Long> {


    @Query("""
             select r from Report r left join fetch r.reportFields left join fetch r.reportTables
                          where r.id=?1
            """)
    @NonNull
    Optional<Report> findById(@NonNull Long id);

    @Query("""
              select r from Report r left join fetch r.reportFields left join fetch r.reportTables
                           where lower(r.name) =lower(?1)
            """)
    Optional<Report> findByName(String name);
}
