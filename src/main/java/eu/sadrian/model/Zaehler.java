package eu.sadrian.model;

import jakarta.persistence.*;

@Entity
public class Zaehler {

    @Id
    @GeneratedValue
    private Long id;
    private int zaehlerNr;
    private String zaehlerName;
    @Enumerated(jakarta.persistence.EnumType.STRING)
    private zaehlerArt zaehlerArt;

    public Zaehler() {
    }

    public Zaehler(int zaehlerNr, eu.sadrian.model.zaehlerArt zaehlerArt, String zaehlerName) {
        this.zaehlerNr = zaehlerNr;
        this.zaehlerArt = zaehlerArt;
        this.zaehlerName = zaehlerName;
    }

    public String getZaehlerName() {
        return zaehlerName;
    }

    public void setZaehlerName(String zaehlerName) {
        this.zaehlerName = zaehlerName;
    }

    public int getZaehlerNr() {
        return zaehlerNr;
    }

    public void setZaehlerNr(int zaehlerNr) {
        this.zaehlerNr = zaehlerNr;
    }

    public eu.sadrian.model.zaehlerArt getZaehlerArt() {
        return zaehlerArt;
    }

    public void setZaehlerArt(eu.sadrian.model.zaehlerArt zaehlerArt) {
        this.zaehlerArt = zaehlerArt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
