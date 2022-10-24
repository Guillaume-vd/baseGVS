package fr.gvs.base.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.gvs.base.model.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    // JPA uses keywords in the function name to construct the query, cf: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
    List<Log> findAllByDateAfter(Date date);
}
