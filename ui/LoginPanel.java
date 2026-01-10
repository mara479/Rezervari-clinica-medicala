package ro.clinicrezervari.ui;

import ro.clinicrezervari.model.Pacient;
import ro.clinicrezervari.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.function.BiConsumer;

public class LoginPanel extends JPanel {

    public LoginPanel(AuthService authService, BiConsumer<Pacient, Boolean> onSuccess) {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Autentificare", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(22f));

        JTextField email = new JTextField(24);
        JPasswordField parola = new JPasswordField(24);
        JButton login = new JButton("Login");

        JLabel hint = new JLabel("Demo: admin@clinic.ro/admin  |  pacient@clinic.ro/1234", SwingConstants.CENTER);

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        add(title, c);

        c.gridwidth = 1;
        c.gridx = 0; c.gridy = 1;
        add(new JLabel("Email:"), c);
        c.gridx = 1;
        add(email, c);

        c.gridx = 0; c.gridy = 2;
        add(new JLabel("Parola:"), c);
        c.gridx = 1;
        add(parola, c);

        c.gridx = 1; c.gridy = 3;
        add(login, c);

        c.gridx = 0; c.gridy = 4; c.gridwidth = 2;
        add(hint, c);

        login.addActionListener(e -> {
            try {
                String em = email.getText().trim();
                String pw = new String(parola.getPassword());

                Pacient p = authService.login(em, pw);
                if (p == null) {
                    JOptionPane.showMessageDialog(this, "Email sau parola incorecta.");
                    return;
                }
                boolean admin = authService.isAdmin(p);
                onSuccess.accept(p, admin);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Eroare BD: " + ex.getMessage());
            }
        });
    }
}
