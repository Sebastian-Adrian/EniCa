package eu.sadrian.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Ablesung {

    @Id
    @GeneratedValue
    private Long id;
    private int zaehlerstand;
    private String datum;

    @ManyToOne
    @JoinColumn(name = "zaehler_id", nullable = false)
    private Zaehler zaehler;

    public Ablesung() {
    }

    public Zaehler getZaehler() {
        return zaehler;
    }

    public void setZaehler(Zaehler zaehler) {
        this.zaehler = zaehler;
    }

    public Ablesung(int zaehlerstand, String datum) {
        this.zaehlerstand = zaehlerstand;
        this.datum = datum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
                ", zaehlerstand=" + zaehlerstand +
                ", datum='" + datum + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ablesung ablesung = (Ablesung) o;

        if (zaehlerstand != ablesung.zaehlerstand) return false;
        if (!Objects.equals(id, ablesung.id)) return false;
        return Objects.equals(datum, ablesung.datum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.zaehlerstand, this.datum);
    }
}
