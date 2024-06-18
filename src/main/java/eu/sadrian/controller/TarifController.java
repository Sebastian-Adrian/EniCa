package eu.sadrian.controller;

import eu.sadrian.model.Tarif;
import eu.sadrian.model.Zaehler;
import eu.sadrian.repository.ZaehlerRepository;
import eu.sadrian.service.TarifService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarife")
public class TarifController {

    private final TarifService tarifService;
    private final ZaehlerRepository zaehlerRepository;

    public TarifController(TarifService tarifService, ZaehlerRepository zaehlerRepository) {
        this.tarifService = tarifService;
        this.zaehlerRepository = zaehlerRepository;
    }

    @GetMapping()
    List<Tarif> all() {
        return tarifService.findAll();
    }

    @PostMapping()
    Tarif newTarif(@RequestBody Tarif newTarif) {
        // Abrufen des Zähler-Objekts anhand der ID
        Long id = newTarif.getZaehler().getId();
        Zaehler zaehler = zaehlerRepository.findById(id)
                .orElseThrow(() -> new ZaehlerNotFoundException(id));

        return tarifService.addTarif(zaehler, newTarif.getGueltigVon(), newTarif.getGueltigBis(), newTarif);
    }

    @PutMapping("/{id}")
    Tarif replaceTarif(@RequestBody Tarif newTarif, @PathVariable Long id) {
        return tarifService.updateTarif(id, newTarif);
    }

    @DeleteMapping("/{id}")
    void deleteTarif(@PathVariable Long id) {
        tarifService.deleteTarif(id);
    }
}
