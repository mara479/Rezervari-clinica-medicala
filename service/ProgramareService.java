package ro.clinicrezervari.service;

import ro.clinicrezervari.db.ProgramareDao;
import ro.clinicrezervari.model.Programare;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Service pentru programari.
 */
public class ProgramareService {

    private final ProgramareDao programareDao;

    public ProgramareService(ProgramareDao programareDao) {
        this.programareDao = programareDao;
    }

    public void creeazaProgramare(Programare p) throws SQLException {
        // regula: nu permitem suprapuneri (aceeasi data + ora + doctor)
        List<Programare> toate = programareDao.findAll();
        for (Programare x : toate) {
            if (x.getIdDoctor() == p.getIdDoctor()
                    && x.getData().equals(p.getData())
                    && x.getOra().equals(p.getOra())
                    && "ACTIVA".equals(x.getStatus())) {
                throw new IllegalStateException("Exista deja o programare la acea ora!");
            }
        }
        programareDao.insert(p);
    }

    public List<Programare> getProgramariPacient(int pacientId) throws SQLException {
        return programareDao.findByPacient(pacientId);
    }

    public List<Programare> getToateProgramarile() throws SQLException {
        return programareDao.findAll();
    }

    public void anuleazaProgramare(int programareId) throws SQLException {
        programareDao.cancel(programareId);
    }

    public void modificaDataOra(int programareId, LocalDate data, LocalTime ora) throws SQLException {
        programareDao.updateDateTime(programareId, data, ora);
    }
}
