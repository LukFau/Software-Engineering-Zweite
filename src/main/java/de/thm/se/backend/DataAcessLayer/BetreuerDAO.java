package de.thm.se.backend.DataAcessLayer;

import de.thm.se.backend.model.Betreuer;
import de.thm.se.backend.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BetreuerDAO {

    // CREATE - Neuen Betreuer anlegen
    public int create(Betreuer betreuer) throws SQLException {
        String sql = """
                INSERT INTO BETREUER
                (vorname = ?, nachname = ?, titel = ?, email = ?, rolle = ?)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, betreuer.getVorname());
            pstmt.setString(2, betreuer.getNachname());
            pstmt.setString(3, betreuer.getTitel());
            pstmt.setString(4, betreuer.getEmail());
            pstmt.setString(5, betreuer.getRolle());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                throw new SQLException("Erstellen fehlgeschlagen, keine ID erhalten");
            }
        }
    }

    // READ - Betreuer nach ID
    public Optional<Betreuer> findById(int betreuer_id) throws SQLException {
        String sql = "SELECT * FROM BETREUER WHERE betreuer_id = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, betreuer_id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Betreuer betreuer = mapResultSet(rs);
                DatabaseConnection.closeResultSet(rs);// ResultSet nach verarbeitung schließen
                return Optional.of(betreuer);
            }
            DatabaseConnection.closeResultSet(rs);
            return Optional.empty();
        }
    }

    // READ - Alle Betreuer
    public List<Betreuer> findAll() throws SQLException {
        String sql = "SELECT * FROM BETREUER ORDER BY nachname";
        List<Betreuer> betreuer = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
                Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                betreuer.add(mapResultSet(rs));
            }
            DatabaseConnection.closeResultSet(rs);
        }
        return betreuer;
    }

    // READ - Betreuer nach Rolle

    // READ - Betreuer nach Vollständiger Name

    // UPDATE - Betreuer aktualisieren
    public boolean update(Betreuer betreuer) throws SQLException {
        String sql = """
                UPDATE BETREUER
                SET vorname = ?, nachname = ?, titel = ?, email = ?, rolle = ?
                WHERE betreuer_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, betreuer.getVorname());
            pstmt.setString(2, betreuer.getNachname());
            pstmt.setString(3, betreuer.getTitel());
            pstmt.setString(4, betreuer.getEmail());
            pstmt.setString(5, betreuer.getRolle());
            pstmt.setInt(6, betreuer.getBetreuerId());

            return pstmt.executeUpdate() > 0;
        }
    }

    // DELETE - Betreuer löschen
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM BETREUER WHERE betreuer_id = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Hilfsmethode: ResultSet in Objekt umwandeln
    private Betreuer mapResultSet(ResultSet rs) throws SQLException {
        Betreuer betreuer = new Betreuer();
        betreuer.setBetreuerId(rs.getInt("betreuer_id"));
        betreuer.setVorname(rs.getString("vorname"));
        betreuer.setNachname(rs.getString("nachname"));
        betreuer.setTitel(rs.getString("titel"));
        betreuer.setEmail(rs.getString("email"));
        betreuer.setRolle(rs.getString("rolle"));
        return betreuer;
    }
}
