package eu.sadrian.controller;

import eu.sadrian.model.Ablesung;
import eu.sadrian.repository.AblesungRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class AblesungController {

    private final AblesungRepository repository;

    AblesungController(AblesungRepository repository) {
        this.repository = repository;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/api/ablesungen")
    List<Ablesung> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/api/ablesungen")
    Ablesung newAblesung(@RequestBody Ablesung newAblesung) {
        return repository.save(newAblesung);
    }

    // Single item
    @GetMapping("/api/ablesungen/{id}")
    EntityModel<Ablesung> one(@PathVariable Long id) {
        Ablesung ablesung = repository.findById(id)
                .orElseThrow(() -> new AblesungNotFoundException(id));

        return EntityModel.of(ablesung,
                linkTo(methodOn(AblesungController.class).one(id)).withSelfRel(),
                linkTo(methodOn(AblesungController.class).all()).withRel("ablesungen"));
    }

    @PutMapping("/api/ablesungen/{id}")
    Ablesung replaceAblesung(@RequestBody Ablesung newAblesung, @PathVariable Long id) {
        return repository.findById(id)
                .map(ablesung -> {
                    ablesung.setZaehlerNr(newAblesung.getZaehlerNr());
                    ablesung.setZaehlerstand(newAblesung.getZaehlerstand());
                    ablesung.setDatum(newAblesung.getDatum());
                    return repository.save(ablesung);
                })
                .orElseGet(() -> {
                    newAblesung.setId(id);
                    return repository.save(newAblesung);
                });
    }

    // tag::delete[]
    @DeleteMapping("/api/ablesungen/{id}")
    void deleteAblesung(@PathVariable Long id) {
        repository.deleteById(id);
    }
    // end::delete[]
}
