package de.thm.se.backend.DataAcessLayer;

import de.thm.se.backend.model.Fachbereich;
import de.thm.se.backend.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FachbereichDAO {

    // CREATE - Neuen Fachbereich anlegen
    public int create(Fachbereich fb) throws SQLException {
        String sql = """
                INSERT INTO FACHBEREICH
                (bezeichnung = ?, fbname = ?)
                VALUES(?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fb.getBezeichnung());
            pstmt.setString(2, fb.getFbname());

            pstmt.executeUpdate();

            try (ResultSet generatedKey = pstmt.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    return generatedKey.getInt(1);
                }
                throw new SQLException("Erstellen fehlgeschlagen, keine ID erhalten.");
            }
        }
    }

    // READ - Fachbereich nach ID
    public Optional<Fachbereich> findById(int id) throws SQLException {
        String sql = "SELECT * FROM FACHBEREICH WHERE fachbereich_id = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Fachbereich fb = mapResultSet(rs);
                DatabaseConnection.closeResultSet(rs);// ResultSet nach verarbeitung schließen
                return Optional.of(fb);
            }
            DatabaseConnection.closeResultSet(rs);
            return Optional.empty();
        }
    }

    // READ - Alle Fachbereiche
    public List<Fachbereich> findAll() throws SQLException {
        String sql = "SELECT * FROM FACHBEREICH";
        List<Fachbereich> fachbereiche = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                fachbereiche.add(mapResultSet(rs));
            }
            DatabaseConnection.closeResultSet(rs);
        }
        return fachbereiche;
    }

    // UPDATE - Fachbereich aktualisieren
    public boolean update(Fachbereich fachbereich) throws SQLException {
        String sql = """
                UPDATE FACHBEREICH
                SET bezeichnung = ?, fbname = ?
                WHERE fachbereich_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fachbereich.getBezeichnung());
            pstmt.setString(2, fachbereich.getFbname());
            pstmt.setInt(3, fachbereich.getFachbereichId());

            return pstmt.executeUpdate() > 0;
        }
    }

    // DELETE - Fachbereich löschen
    public boolean delete(int fbId) throws SQLException {
        String sql = "DELETE FROM FACHBEREICH WHERE fachbereich_id = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, fbId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Hilfsmethode: ResultSet in Objekt umwandeln
    private Fachbereich mapResultSet(ResultSet rs) throws SQLException {
        Fachbereich fachbereich = new Fachbereich();
        fachbereich.setFachbereichId(rs.getInt("fachbereich_id"));
        fachbereich.setBezeichnung(rs.getString("bezeichnung"));
        fachbereich.setFbname(rs.getString("fbname"));
        return fachbereich;
    }
}
