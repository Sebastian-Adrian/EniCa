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

    /**
     * Findet Tarife, die den angegebenen Gültigkeitszeitraum überlappen,
     * schließt jedoch den Tarif mit der angegebenen ID aus.
     *
     * @param zaehlerId Die ID des Zählers, zu dem die Tarife gehören.
     * @param gueltigVon Das Anfangsdatum des Gültigkeitszeitraums.
     * @param gueltigBis Das Enddatum des Gültigkeitszeitraums.
     * @param tarifId Die ID des zu ändernden Tarifs, der ausgeschlossen werden soll.
     * @return Eine Liste von Tarifen, die den angegebenen Zeitraum überlappen.
     */
    @Query("SELECT t FROM Tarif t WHERE t.zaehler.id = :zaehlerId " +
            "AND t.id <> :tarifId " +
            "AND (t.gueltigVon BETWEEN :gueltigVon AND :gueltigBis " +
            "OR t.gueltigBis BETWEEN :gueltigVon AND :gueltigBis " +
            "OR :gueltigVon BETWEEN t.gueltigVon AND t.gueltigBis " +
            "OR :gueltigBis BETWEEN t.gueltigVon AND t.gueltigBis)")
    List<Tarif> findOverlappingTarife(@Param("zaehlerId") Long zaehlerId,
                                      @Param("gueltigVon") LocalDate gueltigVon,
                                      @Param("gueltigBis") LocalDate gueltigBis,
                                      @Param("tarifId") Long tarifId);

    /**
     * Findet Tarife, die den angegebenen Gültigkeitszeitraum überlappen.
     * Diese Methode wird verwendet, wenn ein neuer Tarif erstellt wird und keine Tarif-ID vorhanden ist.
     *
     * @param zaehlerId Die ID des Zählers, zu dem die Tarife gehören.
     * @param gueltigVon Das Anfangsdatum des Gültigkeitszeitraums.
     * @param gueltigBis Das Enddatum des Gültigkeitszeitraums.
     * @return Eine Liste von Tarifen, die den angegebenen Zeitraum überlappen.
     */
    @Query("SELECT t FROM Tarif t WHERE t.zaehler.id = :zaehlerId " +
            "AND (t.gueltigVon BETWEEN :gueltigVon AND :gueltigBis " +
            "OR t.gueltigBis BETWEEN :gueltigVon AND :gueltigBis " +
            "OR :gueltigVon BETWEEN t.gueltigVon AND t.gueltigBis " +
            "OR :gueltigBis BETWEEN t.gueltigVon AND t.gueltigBis)")
    List<Tarif> findOverlappingTarife(@Param("zaehlerId") Long zaehlerId,
                                      @Param("gueltigVon") LocalDate gueltigVon,
                                      @Param("gueltigBis") LocalDate gueltigBis);

    @Query("SELECT t FROM Tarif t WHERE t.zaehler.id = :zaehlerId")
    List<Tarif> findAllByZaehlerId(@Param("zaehlerId") Long zaehlerId);

}

