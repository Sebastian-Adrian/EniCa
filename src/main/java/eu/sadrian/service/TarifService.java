package eu.sadrian.service;

import eu.sadrian.model.Tarif;
import eu.sadrian.model.Zaehler;
import eu.sadrian.repository.TarifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TarifService {

    @Autowired
    private TarifRepository tarifRepository;

    @Transactional
    public Tarif addTarif(Zaehler zaehler, LocalDate gueltigVon, LocalDate gueltigBis, Tarif tarif) {
        List<Tarif> overlappingTarife = tarifRepository.findOverlappingTarife(zaehler.getId(), gueltigVon, gueltigBis);

        if (!overlappingTarife.isEmpty()) {
            throw new IllegalArgumentException("Es gibt bereits einen Tarif in diesem Zeitraum.");
        }

        tarif.setZaehler(zaehler);
        tarif.setGueltigVon(gueltigVon);
        tarif.setGueltigBis(gueltigBis);

        return tarifRepository.save(tarif);
    }

    @Transactional
    public Tarif updateTarif(Long id, Tarif newTarif) {
        Tarif existingTarif = tarifRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarif nicht gefunden"));

        List<Tarif> overlappingTarife = tarifRepository.findOverlappingTarife(existingTarif.getZaehler().getId(), newTarif.getGueltigVon(), newTarif.getGueltigBis());

        if (!overlappingTarife.isEmpty() && overlappingTarife.stream().anyMatch(t -> !t.getId().equals(id))) {
            throw new IllegalArgumentException("Es gibt bereits einen Tarif in diesem Zeitraum.");
        }

        existingTarif.setTarifName(newTarif.getTarifName());
        existingTarif.setPreisProKwh(newTarif.getPreisProKwh());
        existingTarif.setGrundpreis(newTarif.getGrundpreis());
        existingTarif.setGueltigVon(newTarif.getGueltigVon());
        existingTarif.setGueltigBis(newTarif.getGueltigBis());

        return tarifRepository.save(existingTarif);
    }

    public void deleteTarif(Long id) {
        tarifRepository.deleteById(id);
    }

    public List<Tarif> findAll() {
        return tarifRepository.findAll();
    }
}
