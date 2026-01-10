package ro.clinicrezervari.ui;

import ro.clinicrezervari.model.Doctor;
import ro.clinicrezervari.model.Programare;
import ro.clinicrezervari.service.DoctorService;
import ro.clinicrezervari.service.ProgramareService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AdminPanel extends JPanel {

    private final DoctorService doctorService;
    private final ProgramareService programareService;
    private final Runnable onLogout;

    private final JTextField numeField = new JTextField(16);
    private final JTextField specField = new JTextField(16);
    private final JTextField programField = new JTextField(18);

    private final DefaultTableModel progModel = new DefaultTableModel(
            new Object[]{"ID", "PacientID", "DoctorID", "Data", "Ora", "Status"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable progTable = new JTable(progModel);

    public AdminPanel(DoctorService doctorService,
                      ProgramareService programareService,
                      Runnable onLogout) {
        this.doctorService = doctorService;
        this.programareService = programareService;
        this.onLogout = onLogout;

        setLayout(new BorderLayout(10,10));
        add(buildTopBar(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);

        refreshProgramari();
    }

    private JComponent buildTopBar() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel who = new JLabel("Administrator");
        who.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> onLogout.run());

        p.add(who, BorderLayout.WEST);
        p.add(logout, BorderLayout.EAST);
        return p;
    }

    private JComponent buildCenter() {
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setResizeWeight(0.35);

        // sus: adaugare doctor
        JPanel addDoctor = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addDoctor.setBorder(BorderFactory.createTitledBorder("Gestionare doctori"));

        addDoctor.add(new JLabel("Nume:"));
        addDoctor.add(numeField);

        addDoctor.add(new JLabel("Specializare:"));
        addDoctor.add(specField);

        addDoctor.add(new JLabel("Program:"));
        addDoctor.add(programField);

        JButton addBtn = new JButton("Adauga doctor");
        addBtn.addActionListener(e -> addDoctor());

        addDoctor.add(addBtn);

        // jos: programari
        JPanel progr = new JPanel(new BorderLayout(8,8));
        progr.setBorder(BorderFactory.createTitledBorder("Toate programarile"));

        progr.add(new JScrollPane(progTable), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> refreshProgramari());

        JButton cancel = new JButton("Anuleaza programarea selectata");
        cancel.addActionListener(e -> cancelSelected());

        actions.add(refresh);
        actions.add(cancel);

        progr.add(actions, BorderLayout.SOUTH);

        split.setTopComponent(addDoctor);
        split.setBottomComponent(progr);

        return split;
    }

    private void addDoctor() {
        String nume = numeField.getText().trim();
        String spec = specField.getText().trim();
        String prog = programField.getText().trim();

        if (nume.isEmpty() || spec.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completeaza minim nume + specializare.");
            return;
        }

        try {
            Doctor d = new Doctor(0, nume, spec, prog);
            doctorService.adaugaDoctor(d);
            JOptionPane.showMessageDialog(this, "Doctor adaugat!");

            numeField.setText("");
            specField.setText("");
            programField.setText("");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Eroare BD: " + e.getMessage());
        }
    }

    private void refreshProgramari() {
        try {
            progModel.setRowCount(0);
            List<Programare> list = programareService.getToateProgramarile();
            for (Programare p : list) {
                progModel.addRow(new Object[]{
                        p.getId(), p.getIdPacient(), p.getIdDoctor(), p.getData(), p.getOra(), p.getStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Eroare la programari: " + e.getMessage());
        }
    }

    private void cancelSelected() {
        int row = progTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecteaza o programare din tabel.");
            return;
        }
        int progId = (int) progModel.getValueAt(row, 0);

        try {
            programareService.anuleazaProgramare(progId);
            JOptionPane.showMessageDialog(this, "Programare anulata!");
            refreshProgramari();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Eroare BD: " + e.getMessage());
        }
    }
}
