package Domain;

import jakarta.persistence.*;

@Entity
@Table(name = "produs")
public class Produs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProdus;

    @Column(nullable = false)
    private String nume;

    @Column(nullable = false)
    private double pret;

    @Column(nullable = false)
    private int cantitate;

    public Produs() {
        // Constructor fără parametri necesar pentru Hibernate
    }

    public Produs(int idProdus, String nume, double pret, int cantitate) {
        this.idProdus = idProdus;
        this.nume = nume;
        this.pret = pret;
        this.cantitate = cantitate;
    }

    public void actualizeazaStoc(int cantitateVanduta) {
        this.cantitate -= cantitateVanduta;
    }

    public boolean esteDisponibil(int cantitateDorita) {
        return this.cantitate >= cantitateDorita;
    }

    // Getteri și setteri
    public double getPret() {
        return pret;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getIdProdus() {
        return idProdus;
    }

    public void setIdProdus(int idProdus) {
        this.idProdus = idProdus;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }
}
