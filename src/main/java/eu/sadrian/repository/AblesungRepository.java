package eu.sadrian.repository;

import eu.sadrian.model.Ablesung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AblesungRepository extends JpaRepository<Ablesung, Long> {
    List<Ablesung> findByZaehlerId(long id);
}