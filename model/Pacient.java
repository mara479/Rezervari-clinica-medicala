package ro.clinicrezervari.model;

/** Entitate: Pacient */
public class Pacient {
    private int id;
    private String nume;
    private String email;
    private String parola;

    public Pacient() {}

    public Pacient(int id, String nume, String email, String parola) {
        this.id = id;
        this.nume = nume;
        this.email = email;
        this.parola = parola;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getParola() { return parola; }
    public void setParola(String parola) { this.parola = parola; }

    @Override
    public String toString() { return nume + " (" + email + ")"; }
}
