package de.thm.se.backend.DataAcessLayer;

import de.thm.se.backend.model.Semesterzeit;
import de.thm.se.backend.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SemesterzeitDAO {
    // CREATE - Neue Semesterzeit anlegen
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

            try (ResultSet generatedKey = pstmt.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    return generatedKey.getInt(1);
                }
                throw new SQLException("Erstellen fehlgeschlagen, keine ID erhalten.");
            }
        }
    }

    // READ - Semesterzeit nach ID
    public Optional<Semesterzeit> findById(int id) throws SQLException {
        String sql = "SELECT * FROM SEMESTERZEIT WHERE fachbereich_id";

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

    // READ - Alle Semesterzeiten
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

    // UPDATE - Semesterzeit aktualisieren
    public boolean update(Semesterzeit semZeit) throws SQLException {
        String sql = """
                UPDATE SEMESTERZEIT
                SET beginn = ?, ende = ?, bezeichnung = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, java.sql.Date.valueOf(semZeit.getBeginn()));
            pstmt.setDate(2, java.sql.Date.valueOf(semZeit.getEnde()));
            pstmt.setString(3, semZeit.getBezeichnung());

            return pstmt.executeUpdate() > 0;
        }
    }

    // DELETE - Semesterzeit löschen
    public boolean delete(int semZeitId) throws SQLException {
        String sql = "DELETE FROM SEMESTERZEIT WHERE semesterzeit_id = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, semZeitId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Hilfsmethode: ResultSet in Objekt umwandeln
    private Semesterzeit mapResultSet(ResultSet rs) throws SQLException {
        Semesterzeit semZ = new Semesterzeit();
        semZ.setSemesterZeitId(rs.getInt("semesterzeit_id"));
        semZ.setBeginn(rs.getDate("beginn").toLocalDate());
        semZ.setEnde(rs.getDate("ende").toLocalDate());
        semZ.setBezeichnung(rs.getString("bezeichnung"));
        return semZ;
    }
}
