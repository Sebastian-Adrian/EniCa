// In ZaehlerController.java
package eu.sadrian.controller;

import eu.sadrian.model.Zaehler;
import eu.sadrian.repository.ZaehlerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}