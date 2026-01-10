package ro.clinicrezervari.service;

import ro.clinicrezervari.db.DoctorDao;
import ro.clinicrezervari.model.Doctor;

import java.sql.SQLException;
import java.util.List;

/**
 * Service pentru doctori.
 */
public class DoctorService {

    private final DoctorDao doctorDao;

    public DoctorService(DoctorDao doctorDao) {
        this.doctorDao = doctorDao;
    }

    public List<Doctor> getAllDoctori() throws SQLException {
        return doctorDao.findAll();
    }

    public void adaugaDoctor(Doctor doctor) throws SQLException {
        doctorDao.insert(doctor);
    }

    public void modificaDoctor(Doctor doctor) throws SQLException {
        doctorDao.update(doctor);
    }
}
