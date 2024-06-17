package eu.sadrian.controller;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public class ZaehlerNotFoundException extends RuntimeException {
    public ZaehlerNotFoundException(Long id) {
        super("Kann Zähler " + id + " nicht finden.");
    }
}
