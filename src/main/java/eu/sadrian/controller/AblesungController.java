package eu.sadrian.controller;

import eu.sadrian.model.Ablesung;
import eu.sadrian.model.Zaehler;
import eu.sadrian.repository.AblesungRepository;
import eu.sadrian.repository.ZaehlerRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/ablesungen")
public class AblesungController {

    private final AblesungRepository ablesungRepository;
    private final ZaehlerRepository zaehlerRepository;

    AblesungController(AblesungRepository ablesungRepository, ZaehlerRepository zaehlerRepository) {
        this.ablesungRepository = ablesungRepository;
        this.zaehlerRepository = zaehlerRepository;

    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping()
    List<Ablesung> all() {
        return ablesungRepository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping()
    Ablesung newAblesung(@RequestBody Ablesung newAblesung) {
        Long id = newAblesung.getZaehler().getId();
        Zaehler zaehler = zaehlerRepository.findById(id)
                .orElseThrow(() -> new ZaehlerNotFoundException(id));

        newAblesung.setZaehler(zaehler);

        return ablesungRepository.save(newAblesung);

    }

    // Single item
    @GetMapping("/{id}")
    EntityModel<Ablesung> one(@PathVariable Long id) {
        Ablesung ablesung = ablesungRepository.findById(id)
                .orElseThrow(() -> new AblesungNotFoundException(id));

        return EntityModel.of(ablesung,
                linkTo(methodOn(AblesungController.class).one(id)).withSelfRel(),
                linkTo(methodOn(AblesungController.class).all()).withRel("ablesungen"));
    }

    @GetMapping("/zaehler/{id}")
    List<Ablesung> findByZaehlerId(@PathVariable Long id) {
        return ablesungRepository.findByZaehlerId(id);
    }

    @PutMapping("/{id}")
    Ablesung replaceAblesung(@RequestBody Ablesung newAblesung, @PathVariable Long id) {
        return ablesungRepository.findById(id)
                .map(ablesung -> {
                    ablesung.setZaehlerNr(newAblesung.getZaehlerNr());
                    ablesung.setZaehlerstand(newAblesung.getZaehlerstand());
                    ablesung.setDatum(newAblesung.getDatum());
                    return ablesungRepository.save(ablesung);
                })
                .orElseGet(() -> {
                    newAblesung.setId(id);
                    return ablesungRepository.save(newAblesung);
                });
    }

    // tag::delete[]
    @DeleteMapping("/{id}")
    void deleteAblesung(@PathVariable Long id) {
        ablesungRepository.deleteById(id);
    }
    // end::delete[]
}
