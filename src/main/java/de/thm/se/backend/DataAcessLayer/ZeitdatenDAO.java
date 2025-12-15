package de.thm.se.backend.DataAcessLayer;

import de.thm.se.backend.model.Zeitdaten;
import de.thm.se.backend.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ZeitdatenDAO {

    // CREATE - Neue Zeitdaten anlegen
    public int create(Zeitdaten zeit) throws SQLException {
        String sql = """
                INSERT INTO ZEITDATEN
                (arbeit_id, anfangsdatum, abgabedatum, kolloquiumsdatum)
                VALUES(?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, zeit.getArbeitId());
            pstmt.setDate(2, java.sql.Date.valueOf(zeit.getAnfangsdatum()));
            pstmt.setDate(3, java.sql.Date.valueOf(zeit.getAbgabedatum()));
            pstmt.setDate(4, java.sql.Date.valueOf(zeit.getKolloquiumsdatum()));

            pstmt.executeQuery();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                throw new SQLException("Erstellen fehlgeschlagen, keine ID erhalten.");
            }
        }
    }

    // READ - Zeitdaten nach ID
    public Optional<Zeitdaten> findById(int zeitId) throws SQLException {
        String sql = "SELECT * FROM ZEITDATEN WHERE zeit_id = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, zeitId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Zeitdaten zeit = mapResultSet(rs);
                DatabaseConnection.closeResultSet(rs);
                return Optional.of(zeit);
            }
            DatabaseConnection.closeResultSet(rs);
            return Optional.empty();
        }
    }

    // READ - Alle Zeitdaten
    public List<Zeitdaten> findAll() throws SQLException {
        String sql = "SELECT * FROM ZEITDATEN";
        List<Zeitdaten> zeit = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                zeit.add(mapResultSet(rs));
            }
            DatabaseConnection.closeResultSet(rs);
        }
        return zeit;
    }

    // UPDATE - Zeitdaten aktualisieren
    public boolean update(Zeitdaten zeit) throws SQLException {
        String sql = """
                UPDATE ZEITDATEN
                SET arbeit_id = ?, anfangsdatum = ?, abgabedatum = ?, kolloquiumsdatum = ?
                WHERE zeit_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, zeit.getArbeitId());
            pstmt.setDate(2, java.sql.Date.valueOf(zeit.getAnfangsdatum()));
            pstmt.setDate(3, java.sql.Date.valueOf(zeit.getAbgabedatum()));
            pstmt.setDate(4, java.sql.Date.valueOf(zeit.getKolloquiumsdatum()));
            pstmt.setInt(5, zeit.getZeitId());

            return pstmt.executeUpdate() > 0;
        }
    }

    // DELETE - Zeitdaten löschen
    public boolean delete(int zeitId) throws SQLException {
        String sql = "DELETE ZEITDATEN WHERE zeit_id = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, zeitId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Hilfsmethode: ResultSet in Objekt umwandeln
    private Zeitdaten mapResultSet(ResultSet rs) throws SQLException {
        Zeitdaten zeit = new Zeitdaten();
        zeit.setZeitId(rs.getInt("zeit_id"));
        zeit.setArbeitId(rs.getInt("arbeit_id"));
        zeit.setAnfangsdatum(rs.getDate("anfangsdatum").toLocalDate());
        zeit.setAbgabedatum(rs.getDate("abgabedatum").toLocalDate());
        zeit.setKolloquiumsdatum(rs.getDate("kolloquiumsdatum").toLocalDate());
        return zeit;
    }
}
