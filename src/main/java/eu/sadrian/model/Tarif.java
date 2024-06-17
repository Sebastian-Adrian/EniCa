package eu.sadrian.model;

import jakarta.persistence.*;

@Entity
public class Tarif {

    @Id
    @GeneratedValue
    private Long id;
    private String tarifName;
    private double preisProKwh;
    private double grundpreis;

    @ManyToOne
    @JoinColumn(name = "zaehler_id", nullable = false)
    private Zaehler zaehler;

    public Tarif() {
    }

    public Tarif(String tarifName, double preisProKwh, double grundpreis, Zaehler zaehler) {
        this.tarifName = tarifName;
        this.preisProKwh = preisProKwh;
        this.grundpreis = grundpreis;
        this.zaehler = zaehler;
    }

    public Zaehler getZaehler() {
        return zaehler;
    }

    public void setZaehler(Zaehler zaehler) {
        this.zaehler = zaehler;
    }

    public String getTarifName() {
        return tarifName;
    }

    public void setTarifName(String tarifName) {
        this.tarifName = tarifName;
    }

    public double getPreisProKwh() {
        return preisProKwh;
    }

    public void setPreisProKwh(double preisProKwh) {
        this.preisProKwh = preisProKwh;
    }

    public double getGrundpreis() {
        return grundpreis;
    }

    public void setGrundpreis(double grundpreis) {
        this.grundpreis = grundpreis;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
