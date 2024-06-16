// In ZaehlerController.java
package eu.sadrian.controller;

import eu.sadrian.model.Zaehler;
import eu.sadrian.repository.ZaehlerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ZaehlerController {

    private final ZaehlerRepository repository;

    public ZaehlerController(ZaehlerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/zaehler")
    List<Zaehler> all() {
        return repository.findAll();
    }

    @PostMapping("/api/zaehler")
    Zaehler newZaehler(@RequestBody Zaehler newZaehler) {
        return repository.save(newZaehler);
    }

    @PutMapping("/api/zaehler/{id}")
    Zaehler replaceZaehler(@RequestBody Zaehler newZaehler, @PathVariable Long id) {

        return repository.findById(id)
                .map(zaehler -> {
                    zaehler.setZaehlerNr(newZaehler.getZaehlerNr());
                    zaehler.setZaehlerArt(newZaehler.getZaehlerArt());
                    zaehler.setZaehlerName(newZaehler.getZaehlerName());
                    return repository.save(zaehler);
                })
                .orElseGet(() -> {
                    newZaehler.setId(id);
                    return repository.save(newZaehler);
                });
    }

    @DeleteMapping("/api/zaehler/{id}")
    void deleteZaehler(@PathVariable Long id) {
        repository.deleteById(id);
    }
}