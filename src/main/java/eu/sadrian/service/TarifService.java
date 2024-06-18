package eu.sadrian.service;

import eu.sadrian.exception.TarifUeberschneidungException;
import eu.sadrian.model.Tarif;
import eu.sadrian.repository.TarifRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarifService {

    private final TarifRepository tarifRepository;

    public TarifService(TarifRepository tarifRepository) {
        this.tarifRepository = tarifRepository;
    }

    public List<Tarif> findAll() {
        return tarifRepository.findAll();
    }

    public Tarif saveTarif(Tarif newTarif) {
        List<Tarif> overlappingTarife = findOverlappingTarife(newTarif);

        if (!overlappingTarife.isEmpty()) {
            throw new TarifUeberschneidungException("Es gibt bereits einen Tarif in diesem Zeitraum.");
        }

        return tarifRepository.save(newTarif);
    }

    public Tarif updateTarif(Tarif newTarif, Long id) {
        List<Tarif> overlappingTarife = findOverlappingTarife(newTarif);

        if (!overlappingTarife.isEmpty()) {
            overlappingTarife.clear();
            throw new TarifUeberschneidungException("Es gibt bereits einen Tarif in diesem Zeitraum.");
        }

        return tarifRepository.findById(id)
                .map(tarif -> {
                    tarif.setTarifName(newTarif.getTarifName());
                    tarif.setPreisProKwh(newTarif.getPreisProKwh());
                    tarif.setGrundpreis(newTarif.getGrundpreis());
                    tarif.setGueltigVon(newTarif.getGueltigVon());
                    tarif.setGueltigBis(newTarif.getGueltigBis());
                    tarif.setZaehler(newTarif.getZaehler());
                    return tarifRepository.save(tarif);
                })
                .orElseGet(() -> {
                    newTarif.setId(id);
                    return tarifRepository.save(newTarif);
                });
    }

    public void deleteTarif(Long id) {
        tarifRepository.deleteById(id);
    }

    /**
     * Überprüft, ob es überlappende Tarife gibt, die den angegebenen Zeitraum betreffen.
     * Wenn ein bestehender Tarif geändert wird, wird dieser Tarif von der Überprüfung ausgeschlossen.
     *
     * @param tarif Der zu überprüfende Tarif.
     * @return Eine Liste von Tarifen, die den angegebenen Zeitraum überlappen.
     */
    public List<Tarif> findOverlappingTarife(Tarif tarif) {
        if (tarif.getId() != null) {
            // Überprüfung auf überlappende Tarife unter Ausschluss des aktuellen Tarifs (bei Update)
            return tarifRepository.findOverlappingTarife(
                    tarif.getZaehler().getId(),
                    tarif.getGueltigVon(),
                    tarif.getGueltigBis(),
                    tarif.getId()
            );
        } else {
            // Überprüfung auf überlappende Tarife (bei neuer Tarif)
            return tarifRepository.findOverlappingTarife(
                    tarif.getZaehler().getId(),
                    tarif.getGueltigVon(),
                    tarif.getGueltigBis()
            );
        }
    }

}
