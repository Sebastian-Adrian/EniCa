package eu.sadrian.repository;

import eu.sadrian.model.Zaehler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZaehlerRepository extends JpaRepository<Zaehler, Long> {
}
