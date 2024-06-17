package eu.sadrian.controller;

import eu.sadrian.model.Tarif;
import eu.sadrian.model.Zaehler;
import eu.sadrian.repository.TarifRepository;
import eu.sadrian.repository.ZaehlerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarife")
public class TarifController {

    private final TarifRepository tarifRepository;
    private final ZaehlerRepository zaehlerRepository;

    public TarifController(TarifRepository tarifRepository, ZaehlerRepository zaehlerRepository) {
        this.tarifRepository = tarifRepository;
        this.zaehlerRepository = zaehlerRepository;
    }

    @GetMapping()
    List<Tarif> all() {
        return tarifRepository.findAll();
    }

    @PostMapping()
    Tarif newTarif(@RequestBody Tarif newTarif) {
        // Abrufen des Zähler-Objekts anhand der ID
        Long id = newTarif.getZaehler().getId();
        Zaehler zaehler = zaehlerRepository.findById(id)
                .orElseThrow(() -> new ZaehlerNotFoundException(id));

        newTarif.setZaehler(zaehler);

        return tarifRepository.save(newTarif);
    }

    @PutMapping("/{id}")
    Tarif replaceTarif(@RequestBody Tarif newTarif, @PathVariable Long id) {
        return tarifRepository.findById(id)
                .map(tarif -> {
                    tarif.setTarifName(newTarif.getTarifName());
                    tarif.setPreisProKwh(newTarif.getPreisProKwh());
                    tarif.setGrundpreis(newTarif.getGrundpreis());
                    return tarifRepository.save(tarif);
                })
                .orElseGet(() -> {
                    newTarif.setId(id);
                    return tarifRepository.save(newTarif);
                });
    }
    @DeleteMapping("/{id}")
        void deleteTarif(@PathVariable Long id) {
        tarifRepository.deleteById(id);
    }
}
