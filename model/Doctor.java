package ro.clinicrezervari.model;

/** Entitate: Doctor */
public class Doctor {
    private int id;
    private String nume;
    private String specializare;
    private String programDisponibil;

    public Doctor() {}

    public Doctor(int id, String nume, String specializare, String programDisponibil) {
        this.id = id;
        this.nume = nume;
        this.specializare = specializare;
        this.programDisponibil = programDisponibil;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getSpecializare() { return specializare; }
    public void setSpecializare(String specializare) { this.specializare = specializare; }

    public String getProgramDisponibil() { return programDisponibil; }
    public void setProgramDisponibil(String programDisponibil) { this.programDisponibil = programDisponibil; }

    @Override
    public String toString() { return nume + " - " + specializare; }
}
