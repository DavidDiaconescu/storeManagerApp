package Domain;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "comanda")
public class Comanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idComanda;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataComanda;

    private int cantitate;

    private boolean livrata = false; // ✅ nou: statusul comenzii (livrată sau nu)

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "comanda_produs",
            joinColumns = @JoinColumn(name = "idComanda"),
            inverseJoinColumns = @JoinColumn(name = "idProdus")
    )
    private List<Produs> produse;

    public Comanda() {
        this.produse = new ArrayList<>();
        this.dataComanda = new Date();
    }

    public Comanda(int idComanda, Date dataComanda) {
        this.idComanda = idComanda;
        this.dataComanda = dataComanda;
        this.produse = new ArrayList<>();
    }

    public void adaugaProdus(Produs produs, int cantitate) {
        if (produs.esteDisponibil(cantitate)) {
            produse.add(produs);
            this.cantitate += cantitate;
            produs.actualizeazaStoc(cantitate);
        } else {
            System.out.println("Produsul nu este disponibil în cantitatea dorită.");
        }
    }

    public double calculeazaTotal() {
        return produse.stream().mapToDouble(Produs::getPret).sum();
    }

    public List<Produs> getProduse() {
        return produse;
    }

    public void setProduse(List<Produs> produse) {
        this.produse = produse;
    }

    public int getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(int idComanda) {
        this.idComanda = idComanda;
    }

    public Date getDataComanda() {
        return dataComanda;
    }

    public void setDataComanda(Date dataComanda) {
        this.dataComanda = dataComanda;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public boolean isLivrata() {
        return livrata;
    }

    public void setLivrata(boolean livrata) {
        this.livrata = livrata;
    }

    @Transient // Hibernate nu va salva acest câmp (pentru simplitate)
    private Map<Produs, Integer> cantitati = new HashMap<>();




}
