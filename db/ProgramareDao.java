package ro.clinicrezervari.db;

import ro.clinicrezervari.model.Programare;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pentru tabela PROGRAMARE.
 */
public class ProgramareDao {

    public Programare insert(Programare p) throws SQLException {
        String sql = "INSERT INTO PROGRAMARE(pacient_id, doctor_id, data_programare, ora_programare, durata_min, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, p.getIdPacient());
            ps.setInt(2, p.getIdDoctor());
            ps.setDate(3, Date.valueOf(p.getData()));
            ps.setTime(4, Time.valueOf(p.getOra()));
            ps.setInt(5, 30);
            ps.setString(6, p.getStatus());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getInt(1));
            }
        }
        return p;
    }

    public List<Programare> findByPacient(int pacientId) throws SQLException {
        String sql = "SELECT id, pacient_id, doctor_id, data_programare, ora_programare, status " +
                "FROM PROGRAMARE WHERE pacient_id=? ORDER BY data_programare DESC, ora_programare DESC";
        return selectList(sql, pacientId);
    }

    public List<Programare> findAll() throws SQLException {
        String sql = "SELECT id, pacient_id, doctor_id, data_programare, ora_programare, status " +
                "FROM PROGRAMARE ORDER BY data_programare DESC, ora_programare DESC";
        return selectList(sql);
    }

    public void cancel(int programareId) throws SQLException {
        String sql = "UPDATE PROGRAMARE SET status='ANULATA' WHERE id=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, programareId);
            ps.executeUpdate();
        }
    }

    public void updateDateTime(int programareId, LocalDate data, LocalTime ora) throws SQLException {
        String sql = "UPDATE PROGRAMARE SET data_programare=?, ora_programare=? WHERE id=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(data));
            ps.setTime(2, Time.valueOf(ora));
            ps.setInt(3, programareId);
            ps.executeUpdate();
        }
    }

    private List<Programare> selectList(String sql, Object... params) throws SQLException {
        List<Programare> list = new ArrayList<>();
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Programare(
                            rs.getInt("id"),
                            rs.getInt("pacient_id"),
                            rs.getInt("doctor_id"),
                            rs.getDate("data_programare").toLocalDate(),
                            rs.getTime("ora_programare").toLocalTime(),
                            rs.getString("status")
                    ));
                }
            }
        }
        return list;
    }
}