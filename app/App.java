package ro.clinicrezervari.app;

import ro.clinicrezervari.db.DatabaseManager;
import ro.clinicrezervari.db.DoctorDao;
import ro.clinicrezervari.db.PacientDao;
import ro.clinicrezervari.model.Doctor;
import ro.clinicrezervari.ui.MainFrame;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        DatabaseManager.initSchema();
        seed();

        SwingUtilities.invokeLater(() -> {
            MainFrame f = new MainFrame();
            f.setVisible(true);
        });
    }

    private static void seed() {
        try {
            DoctorDao doctorDao = new DoctorDao();
            if (doctorDao.findAll().isEmpty()) {
                doctorDao.insert(new Doctor(0, "Dr. Ionescu", "Cardiologie", "L-V 09:00-15:00"));
                doctorDao.insert(new Doctor(0, "Dr. Pop", "Dermatologie", "L-V 10:00-16:00"));
            }

            PacientDao pacientDao = new PacientDao();
            if (pacientDao.findByEmailAndPassword("admin@clinic.ro", "admin") == null) {
                pacientDao.insert("Admin", "admin@clinic.ro", "admin", true);
            }
            if (pacientDao.findByEmailAndPassword("pacient@clinic.ro", "1234") == null) {
                pacientDao.insert("Pacient Demo", "pacient@clinic.ro", "1234", false);
            }
        } catch (Exception ignored) {}
    }
}
