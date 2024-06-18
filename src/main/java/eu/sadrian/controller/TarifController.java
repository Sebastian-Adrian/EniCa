package eu.sadrian.controller;

import eu.sadrian.exception.TarifUeberschneidungException;
import eu.sadrian.exception.ZaehlerNotFoundException;
import eu.sadrian.model.Tarif;
import eu.sadrian.model.Zaehler;
import eu.sadrian.repository.ZaehlerRepository;
import eu.sadrian.service.TarifService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<?> newTarif(@RequestBody Tarif newTarif) {
        try {
            // Abrufen des Zähler-Objekts anhand der ID
            Long id = newTarif.getZaehler().getId();
            Zaehler zaehler = zaehlerRepository.findById(id)
                    .orElseThrow(() -> new ZaehlerNotFoundException(id));

            newTarif.setZaehler(zaehler);

            return new ResponseEntity<>(tarifService.saveTarif(newTarif), HttpStatus.CREATED);
        } catch (TarifUeberschneidungException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<?> replaceTarif(@RequestBody Tarif newTarif, @PathVariable Long id) {
        try {
            return new ResponseEntity<>(tarifService.updateTarif(newTarif, id), HttpStatus.OK);
        } catch (TarifUeberschneidungException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    void deleteTarif(@PathVariable Long id) {
        tarifService.deleteTarif(id);
    }
}
