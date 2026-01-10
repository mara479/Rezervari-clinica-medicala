package ro.clinicrezervari.service;

import ro.clinicrezervari.db.PacientDao;
import ro.clinicrezervari.model.Pacient;

import java.sql.SQLException;

/**
 * Service pentru autentificare.
 */
public class AuthService {

    private final PacientDao pacientDao;

    public AuthService(PacientDao pacientDao) {
        this.pacientDao = pacientDao;
    }

    public Pacient login(String email, String parola) throws SQLException {
        return pacientDao.findByEmailAndPassword(email, parola);
    }

    public boolean isAdmin(Pacient pacient) throws SQLException {
        return pacientDao.isAdmin(pacient.getId());
    }
}
