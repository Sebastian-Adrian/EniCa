package eu.sadrian.controller;

import eu.sadrian.model.Ablesung;
import eu.sadrian.repository.AblesungRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/ablesungen")
public class AblesungController {

    private final AblesungRepository repository;

    AblesungController(AblesungRepository repository) {
        this.repository = repository;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping()
    List<Ablesung> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping()
    Ablesung newAblesung(@RequestBody Ablesung newAblesung) {
        return repository.save(newAblesung);
    }

    // Single item
    @GetMapping("/{id}")
    EntityModel<Ablesung> one(@PathVariable Long id) {
        Ablesung ablesung = repository.findById(id)
                .orElseThrow(() -> new AblesungNotFoundException(id));

        return EntityModel.of(ablesung,
                linkTo(methodOn(AblesungController.class).one(id)).withSelfRel(),
                linkTo(methodOn(AblesungController.class).all()).withRel("ablesungen"));
    }

    @GetMapping("/zaehler/{zaehlerNr}")
    List<Ablesung> findByZaehlerNr(@PathVariable int zaehlerNr) {
        return repository.findByZaehlerNr(zaehlerNr);
    }

    @PutMapping("/{id}")
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
    @DeleteMapping("/{id}")
    void deleteAblesung(@PathVariable Long id) {
        repository.deleteById(id);
    }
    // end::delete[]
}
