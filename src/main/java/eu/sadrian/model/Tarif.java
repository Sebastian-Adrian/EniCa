package eu.sadrian.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Tarif {

    @Id
    @GeneratedValue
    private Long id;
    private String tarifName;
    private double preisProKwh;
    private double grundpreis;

    public Tarif() {
    }

    public Tarif(String tarifName, double preisProKwh, double grundpreis) {
        this.tarifName = tarifName;
        this.preisProKwh = preisProKwh;
        this.grundpreis = grundpreis;
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
