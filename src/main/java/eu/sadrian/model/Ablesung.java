package eu.sadrian.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Ablesung {

    @Id
    @GeneratedValue
    private Long id;
    private int zaehlerNr;
    private int zaehlerstand;
    private int tmpZaehlerstand;
    private boolean editMode;
    private String datum;

    public int getTmpZaehlerstand() {
        return tmpZaehlerstand;
    }

    public void setTmpZaehlerstand(int tmpZaehlerstand) {
        this.tmpZaehlerstand = tmpZaehlerstand;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public Ablesung() {
    }

    public Ablesung(int zaehlerNr, int zaehlerstand, String datum) {
        this.zaehlerNr = zaehlerNr;
        this.zaehlerstand = zaehlerstand;
        this.datum = datum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getZaehlerNr() {
        return zaehlerNr;
    }

    public void setZaehlerNr(int zaehlerNr) {
        this.zaehlerNr = zaehlerNr;
    }

    public int getZaehlerstand() {
        return zaehlerstand;
    }

    public void setZaehlerstand(int zaehlerstand) {
        this.zaehlerstand = zaehlerstand;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    @Override
    public String toString() {
        return "Ablesung{" +
                "id=" + id +
                ", zaehlerNr=" + zaehlerNr +
                ", zaehlerstand=" + zaehlerstand +
                ", datum='" + datum + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ablesung ablesung = (Ablesung) o;

        if (zaehlerNr != ablesung.zaehlerNr) return false;
        if (zaehlerstand != ablesung.zaehlerstand) return false;
        if (!Objects.equals(id, ablesung.id)) return false;
        return Objects.equals(datum, ablesung.datum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.zaehlerNr, this.zaehlerstand, this.datum);
    }
}
