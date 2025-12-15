package de.thm.se.backend.DataAcessLayer;

import de.thm.se.backend.model.Semesterzeit;
import de.thm.se.backend.util.DatabaseConnection;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SemesterzeitDAO {
    public int create(Semesterzeit semZ) throws SQLException {
        String sql = """
                INSERT INTO SEMESTERZEIT
                (beginn, ende, bezeichnung)
                VALUES(?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, java.sql.Date.valueOf(semZ.getBeginn()));
            pstmt.setDate(2, java.sql.Date.valueOf(semZ.getEnde()));
            pstmt.setString(3, semZ.getBezeichnung());
            pstmt.executeUpdate();

            // WORKAROUND
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new SQLException("Erstellen fehlgeschlagen, keine ID erhalten.");
            }
        }
    }

    public Optional<Semesterzeit> findById(int id) throws SQLException {
        String sql = "SELECT * FROM SEMESTERZEIT WHERE semesterzeit_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Semesterzeit semZ = mapResultSet(rs);
                DatabaseConnection.closeResultSet(rs);
                return Optional.of(semZ);
            }
            DatabaseConnection.closeResultSet(rs);
            return Optional.empty();
        }
    }

    public List<Semesterzeit> findAll() throws SQLException {
        String sql = "SELECT * FROM SEMESTERZEIT";
        List<Semesterzeit> semZeiten = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                semZeiten.add(mapResultSet(rs));
            }
            DatabaseConnection.closeResultSet(rs);
        }
        return semZeiten;
    }

    public boolean update(Semesterzeit semZeit) throws SQLException {
        String sql = """
                UPDATE SEMESTERZEIT
                SET beginn = ?, ende = ?, bezeichnung = ?
                WHERE semesterzeit_id = ?
                """;
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, java.sql.Date.valueOf(semZeit.getBeginn()));
            pstmt.setDate(2, java.sql.Date.valueOf(semZeit.getEnde()));
            pstmt.setString(3, semZeit.getBezeichnung());
            pstmt.setInt(4, semZeit.getSemesterZeitId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int semZeitId) throws SQLException {
        String sql = "DELETE FROM SEMESTERZEIT WHERE semesterzeit_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, semZeitId);
            return pstmt.executeUpdate() > 0;
        }
    }

    private Semesterzeit mapResultSet(ResultSet rs) throws SQLException {
        Semesterzeit semZ = new Semesterzeit();
        semZ.setSemesterZeitId(rs.getInt("semesterzeit_id"));
        semZ.setBeginn(rs.getDate("beginn").toLocalDate());
        semZ.setEnde(rs.getDate("ende").toLocalDate());
        semZ.setBezeichnung(rs.getString("bezeichnung"));
        return semZ;
    }
}