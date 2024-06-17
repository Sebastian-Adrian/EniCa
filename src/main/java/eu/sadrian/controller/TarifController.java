package eu.sadrian.controller;

import eu.sadrian.model.Tarif;
import eu.sadrian.repository.TarifRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarife")
public class TarifController {

    private final TarifRepository repository;

    public TarifController(TarifRepository repository) {
        this.repository = repository;
    }

    @GetMapping()
    List<Tarif> all() {
        return repository.findAll();
    }

    @PostMapping()
    Tarif newTarif(@RequestBody Tarif newTarif) {
        return repository.save(newTarif);
    }

    @PutMapping("/{id}")
    Tarif replaceTarif(@RequestBody Tarif newTarif, @PathVariable Long id) {
        return repository.findById(id)
                .map(tarif -> {
                    tarif.setTarifName(newTarif.getTarifName());
                    tarif.setPreisProKwh(newTarif.getPreisProKwh());
                    tarif.setGrundpreis(newTarif.getGrundpreis());
                    return repository.save(tarif);
                })
                .orElseGet(() -> {
                    newTarif.setId(id);
                    return repository.save(newTarif);
                });
    }
    @DeleteMapping("/{id}")
        void deleteTarif(@PathVariable Long id) {
            repository.deleteById(id);
    }
}
