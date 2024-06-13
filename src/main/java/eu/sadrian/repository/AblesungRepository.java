package eu.sadrian.repository;

import eu.sadrian.model.Ablesung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AblesungRepository extends JpaRepository<Ablesung, Long> {
}