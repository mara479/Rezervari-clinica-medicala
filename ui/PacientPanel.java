package ro.clinicrezervari.ui;

import ro.clinicrezervari.model.Doctor;
import ro.clinicrezervari.model.Pacient;
import ro.clinicrezervari.model.Programare;
import ro.clinicrezervari.service.DoctorService;
import ro.clinicrezervari.service.ProgramareService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PacientPanel extends JPanel {

    private final Pacient pacient;
    private final DoctorService doctorService;
    private final ProgramareService programareService;
    private final Runnable onLogout;

    private final DefaultTableModel doctorModel = new DefaultTableModel(
            new Object[]{"ID", "Nume", "Specializare", "Program"}, 0) {
        public boolean isCellEditable(int row, int col) { return false; }
    };
    private final JTable doctorTable = new JTable(doctorModel);

    private final DefaultTableModel progModel = new DefaultTableModel(
            new Object[]{"ID", "DoctorID", "Data", "Ora", "Status"}, 0) {
        public boolean isCellEditable(int row, int col) { return false; }
    };
    private final JTable progTable = new JTable(progModel);

    private final JTextField dataField = new JTextField("2026-01-10", 10);
    private final JTextField oraField = new JTextField("10:00", 10);

    public PacientPanel(Pacient pacient,
                        DoctorService doctorService,
                        ProgramareService programareService,
                        Runnable onLogout) {
        this.pacient = pacient;
        this.doctorService = doctorService;
        this.programareService = programareService;
        this.onLogout = onLogout;

        setLayout(new BorderLayout(10, 10));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);

        refreshDoctori();
        refreshProgramari();
    }

    private JComponent buildTopBar() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel who = new JLabel("Pacient: " + pacient.getNume() + " (" + pacient.getEmail() + ")");
        who.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> onLogout.run());

        p.add(who, BorderLayout.WEST);
        p.add(logout, BorderLayout.EAST);
        return p;
    }

    private JComponent buildCenter() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.55);

        // stânga: doctori + creare programare
        JPanel left = new JPanel(new BorderLayout(8,8));
        left.setBorder(BorderFactory.createTitledBorder("Doctori disponibili"));

        JScrollPane docScroll = new JScrollPane(doctorTable);
        left.add(docScroll, BorderLayout.CENTER);

        JPanel create = new JPanel(new FlowLayout(FlowLayout.LEFT));
        create.add(new JLabel("Data (YYYY-MM-DD):"));
        create.add(dataField);
        create.add(new JLabel("Ora (HH:MM):"));
        create.add(oraField);

        JButton createBtn = new JButton("Creeaza programare la doctor selectat");
        createBtn.addActionListener(e -> createProgramare());
        create.add(createBtn);

        JButton refreshDoc = new JButton("Refresh doctori");
        refreshDoc.addActionListener(e -> refreshDoctori());
        create.add(refreshDoc);

        left.add(create, BorderLayout.SOUTH);

        // dreapta: programarile mele
        JPanel right = new JPanel(new BorderLayout(8,8));
        right.setBorder(BorderFactory.createTitledBorder("Istoric programari"));

        JScrollPane progScroll = new JScrollPane(progTable);
        right.add(progScroll, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshProg = new JButton("Refresh programari");
        refreshProg.addActionListener(e -> refreshProgramari());

        JButton cancelBtn = new JButton("Anuleaza programarea selectata");
        cancelBtn.addActionListener(e -> cancelSelected());

        actions.add(refreshProg);
        actions.add(cancelBtn);

        right.add(actions, BorderLayout.SOUTH);

        split.setLeftComponent(left);
        split.setRightComponent(right);
        return split;
    }

    private void refreshDoctori() {
        try {
            doctorModel.setRowCount(0);
            List<Doctor> docs = doctorService.getAllDoctori();
            for (Doctor d : docs) {
                doctorModel.addRow(new Object[]{
                        d.getId(), d.getNume(), d.getSpecializare(), d.getProgramDisponibil()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Eroare la doctori: " + e.getMessage());
        }
    }

    private void refreshProgramari() {
        try {
            progModel.setRowCount(0);
            List<Programare> list = programareService.getProgramariPacient(pacient.getId());
            for (Programare p : list) {
                progModel.addRow(new Object[]{
                        p.getId(), p.getIdDoctor(), p.getData(), p.getOra(), p.getStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Eroare la programari: " + e.getMessage());
        }
    }

    private void createProgramare() {
        int row = doctorTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecteaza un doctor din tabel.");
            return;
        }

        int doctorId = (int) doctorModel.getValueAt(row, 0);

        try {
            LocalDate data = LocalDate.parse(dataField.getText().trim());
            LocalTime ora = LocalTime.parse(oraField.getText().trim());

            Programare p = new Programare();
            p.setIdPacient(pacient.getId());
            p.setIdDoctor(doctorId);
            p.setData(data);
            p.setOra(ora);
            p.setStatus("ACTIVA");

            programareService.creeazaProgramare(p);

            JOptionPane.showMessageDialog(this, "Programare creata!");
            refreshProgramari();

        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Date/ora invalide sau eroare BD: " + ex.getMessage());
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
