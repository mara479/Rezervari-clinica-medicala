package ro.clinicrezervari.model;

/** Administrator = Pacient cu rol special */
public class Administrator extends Pacient {
    public Administrator() {}

    public Administrator(int id, String nume, String email, String parola) {
        super(id, nume, email, parola);
    }
}
