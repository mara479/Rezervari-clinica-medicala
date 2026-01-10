package ro.clinicrezervari.model;

import java.time.LocalDate;
import java.time.LocalTime;

/** Entitate: Programare */
public class Programare {
    private int id;
    private int idPacient;
    private int idDoctor;
    private LocalDate data;
    private LocalTime ora;
    private String status; // ACTIVA / ANULATA

    public Programare() {}

    public Programare(int id, int idPacient, int idDoctor, LocalDate data, LocalTime ora, String status) {
        this.id = id;
        this.idPacient = idPacient;
        this.idDoctor = idDoctor;
        this.data = data;
        this.ora = ora;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPacient() { return idPacient; }
    public void setIdPacient(int idPacient) { this.idPacient = idPacient; }

    public int getIdDoctor() { return idDoctor; }
    public void setIdDoctor(int idDoctor) { this.idDoctor = idDoctor; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public LocalTime getOra() { return ora; }
    public void setOra(LocalTime ora) { this.ora = ora; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void anuleazaProgramare() { this.status = "ANULATA"; }

    @Override
    public String toString() {
        return data + " " + ora + " (doctorId=" + idDoctor + ", " + status + ")";
    }
}
