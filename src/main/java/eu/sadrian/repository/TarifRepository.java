package eu.sadrian.repository;

import eu.sadrian.model.Tarif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TarifRepository extends JpaRepository<Tarif, Long> {
    // Methode zum Überprüfen von überschneidenden Gültigkeitszeiträumen für Tarife des Zählers
    @Query("SELECT t FROM Tarif t WHERE t.zaehler.id = :zaehlerId AND " +
            "(t.gueltigVon < :gueltigBis AND t.gueltigBis > :gueltigVon)")
    List<Tarif> findOverlappingTarife(@Param("zaehlerId") Long zaehlerId,
                                      @Param("gueltigVon") LocalDate gueltigVon,
                                      @Param("gueltigBis") LocalDate gueltigBis);
}

