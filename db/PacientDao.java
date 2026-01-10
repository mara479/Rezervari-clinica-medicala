package ro.clinicrezervari.db;

import ro.clinicrezervari.model.Administrator;
import ro.clinicrezervari.model.Pacient;

import java.sql.*;

/**
 * DAO pentru tabela PACIENT (include login).
 */
public class PacientDao {

    public Pacient insert(String nume, String email, String parola, boolean isAdmin) throws SQLException {
        String sql = "INSERT INTO PACIENT(nume, email, parola, is_admin) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nume);
            ps.setString(2, email);
            ps.setString(3, parola);
            ps.setInt(4, isAdmin ? 1 : 0);
            ps.executeUpdate();

            int id;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                id = rs.getInt(1);
            }
            return isAdmin ? new Administrator(id, nume, email, parola) : new Pacient(id, nume, email, parola);
        }
    }

    public Pacient findByEmailAndPassword(String email, String parola) throws SQLException {
        String sql = "SELECT id, nume, email, parola, is_admin FROM PACIENT WHERE email=? AND parola=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, parola);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                int id = rs.getInt("id");
                String nume = rs.getString("nume");
                String em = rs.getString("email");
                String pw = rs.getString("parola");
                boolean admin = rs.getInt("is_admin") == 1;

                return admin ? new Administrator(id, nume, em, pw) : new Pacient(id, nume, em, pw);
            }
        }
    }

    public boolean isAdmin(int pacientId) throws SQLException {
        String sql = "SELECT is_admin FROM PACIENT WHERE id=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pacientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false;
                return rs.getInt(1) == 1;
            }
        }
    }
}