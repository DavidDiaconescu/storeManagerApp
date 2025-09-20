package Service;

import Domain.Agent;
import Domain.Comanda;
import Domain.Produs;
import Repo.AgentRepository;
import Repo.ComandaRepository;
import Repo.ProdusRepository;

import java.util.List;
import java.util.Optional;

public class Service {

    private final AgentRepository agentRepo;
    private final ProdusRepository produsRepo;
    private final ComandaRepository comandaRepo;

    public Service() {
        this.agentRepo = new AgentRepository();
        this.produsRepo = new ProdusRepository();
        this.comandaRepo = new ComandaRepository();
    }

    // ----------------- Funcții pentru Agent -----------------
    public boolean loginAgent(String email, String parola) {
        List<Agent> agenti = agentRepo.readAll();  // corect: fără static
        for (Agent agent : agenti) {
            if (agent.getEmail().equals(email) && agent.getParola().equals(parola)) {
                return true;
            }
        }
        return false;
    }

    public boolean createAgentAccount(String numeComplet, String email, String parola) {
        Optional<Agent> existent = agentRepo.readAll().stream()
                .filter(agent -> agent.getEmail().equalsIgnoreCase(email))
                .findFirst();

        if (existent.isPresent()) {
            return false; // deja există utilizatorul
        }

        Agent agent = new Agent(numeComplet, email, parola);
        agentRepo.save(agent); // ← asigură-te că metoda save() există în AgentRepository
        return true;
    }

    // ----------------- Funcții pentru Produse -----------------

    public void adaugaProdus(String nume, double pret, int cantitate) {
        Produs produs = new Produs(0, nume, pret, cantitate); // ID 0 deoarece Hibernate îl va genera
        produsRepo.create(produs);
    }

    public List<Produs> getListaProduse() {
        return produsRepo.readAll();
    }

    public void actualizeazaCantitateProdus(int idProdus, int cantitateNoua) {
        produsRepo.readById(idProdus).ifPresent(p -> {
            p.setCantitate(cantitateNoua);
            produsRepo.update(p);
        });
    }

    public void stergeProdus(int idProdus) {
        produsRepo.delete(idProdus);
    }

    // ----------------- Funcții pentru Comenzi -----------------

    public int creeazaComanda(int idAgent, String status) {
        Comanda comanda = new Comanda();  // ID neinițializat
        comanda.setCantitate(0);
        comandaRepo.create(comanda);      // ID-ul va fi setat automat aici
        return comanda.getIdComanda();    // îl extragem din obiectul deja populat
    }



    public void adaugaProdusLaComanda(int idComanda, int idProdus, int cantitate) {
        Optional<Comanda> comandaOpt = comandaRepo.readById(idComanda);
        Optional<Produs> produsOpt = produsRepo.readById(idProdus);

        if (comandaOpt.isPresent() && produsOpt.isPresent()) {
            Comanda comanda = comandaOpt.get();
            Produs produs = produsOpt.get();
            comanda.adaugaProdus(produs, cantitate);
            comandaRepo.update(comanda);
            produsRepo.update(produs); // actualizăm și stocul produsului
        }
    }

    public void actualizeazaStatusComanda(int idComanda, String status) {
        comandaRepo.readById(idComanda).ifPresent(c -> {
            // presupunem că vei avea un câmp status în viitor
            // c.setStatus(status);
            comandaRepo.update(c);
        });
    }
    public List<Comanda> getComenzi() {
        return comandaRepo.readAll();
    }
    public void marcheazaComandaCaLivrata(int idComanda) {
        comandaRepo.readById(idComanda).ifPresent(c -> {
            c.setLivrata(true);
            comandaRepo.update(c);
        });
    }


}
