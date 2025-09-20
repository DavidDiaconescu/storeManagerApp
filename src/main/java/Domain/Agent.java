package Domain;

import jakarta.persistence.*;

@Entity
@Table(name = "agent")
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAgent;

    @Column(nullable = false)
    private String nume;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String parola;

    public Agent() {
    }

    public Agent(String nume, String email, String parola) {
        this.nume = nume;
        this.email = email;
        this.parola = parola;
    }

    // Getter & Setter
    public int getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(int idAgent) {
        this.idAgent = idAgent;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }
}
