package ro.clinicrezervari.db;

import ro.clinicrezervari.model.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDao {
    public Doctor insert(Doctor d) throws SQLException {
        String sql = "INSERT INTO DOCTOR(nume, specializare, program_disponibil) VALUES (?, ?, ?)";
        try (Connection con = DatabaseManager.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getNume());
            ps.setString(2, d.getSpecializare());
            ps.setString(3, d.getProgramDisponibil());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    d.setId(rs.getInt(1));
                }
            }

        }
        return d;
    }

    public List<Doctor> findAll() throws SQLException{
        List<Doctor> list= new ArrayList<>();
        String sql = "SELECT id, nume, specializare, program_disponibil FROM DOCTOR ORDER BY nume";
        try (Connection con = DatabaseManager.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                list.add(new Doctor(rs.getInt("id"), rs.getString("nume"), rs.getString("specializare"), rs.getString("program_disponibil")));

            }
        }
        return list;
    }


    public void update(Doctor d) throws SQLException {
        String sql = "UPDATE DOCTOR SET nume=?, specializare=?, program_disponibil=? WHERE id=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, d.getNume());
            ps.setString(2, d.getSpecializare());
            ps.setString(3, d.getProgramDisponibil());
            ps.setInt(4, d.getId());
            ps.executeUpdate();
        }
    }
}
