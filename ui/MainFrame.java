package ro.clinicrezervari.ui;

import ro.clinicrezervari.db.DoctorDao;
import ro.clinicrezervari.db.PacientDao;
import ro.clinicrezervari.db.ProgramareDao;
import ro.clinicrezervari.model.Pacient;
import ro.clinicrezervari.service.AuthService;
import ro.clinicrezervari.service.DoctorService;
import ro.clinicrezervari.service.ProgramareService;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel root = new JPanel(cardLayout);

    private final AuthService authService;
    private final DoctorService doctorService;
    private final ProgramareService programareService;

    public MainFrame() {
        // servicii
        this.authService = new AuthService(new PacientDao());
        this.doctorService = new DoctorService(new DoctorDao());
        this.programareService = new ProgramareService(new ProgramareDao());

        setTitle("Clinica - Sistem rezervari");
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ecrane
        LoginPanel loginPanel = new LoginPanel(authService, this::onLoginSuccess);

        root.add(loginPanel, "LOGIN");
        setContentPane(root);
        cardLayout.show(root, "LOGIN");
    }

    private void onLoginSuccess(Pacient pacient, boolean isAdmin) {
        if (isAdmin) {
            AdminPanel adminPanel = new AdminPanel(doctorService, programareService, this::logout);
            root.add(adminPanel, "ADMIN");
            cardLayout.show(root, "ADMIN");
        } else {
            PacientPanel pacientPanel = new PacientPanel(pacient, doctorService, programareService, this::logout);
            root.add(pacientPanel, "PACIENT");
            cardLayout.show(root, "PACIENT");
        }
        revalidate();
        repaint();
    }

    private void logout() {
        cardLayout.show(root, "LOGIN");
        revalidate();
        repaint();
    }
}
