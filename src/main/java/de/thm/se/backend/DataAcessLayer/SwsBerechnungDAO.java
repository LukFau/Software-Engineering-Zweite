package de.thm.se.backend.DataAcessLayer;

import de.thm.se.backend.model.SwsBerechnung;
import de.thm.se.backend.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SwsBerechnungDAO {

    // CREATE - Neue SWS Berechnung
    public int create(SwsBerechnung swsBerechnung) throws SQLException {
        String sql = """
                INSERT INTO SWS_BERECHNUNG
                (arbet_id, betreuer_id, semester_id, pruefungsordnungId, sws_wert, rolle, berechnet_am)
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, swsBerechnung.getArbeitId());
            pstmt.setInt(2, swsBerechnung.getBetreuerId());
            pstmt.setInt(3, swsBerechnung.getSemesterId());
            pstmt.setInt(4, swsBerechnung.getPruefungsordnungId());
            pstmt.setFloat(5, swsBerechnung.getSwsWert());
            pstmt.setString(6, swsBerechnung.getRolle());
            pstmt.setDate(7, java.sql.Date.valueOf(swsBerechnung.getBerechnetAm()));

            pstmt.executeUpdate();

            try (ResultSet generaretedKey = pstmt.getGeneratedKeys()) {
                if (generaretedKey.next()) {
                    return generaretedKey.getInt(sql);
                }
                throw new SQLException("Erstellen fehlgeschlagen, keine ID erhalten.");
            }
        }
    }

    // READ - SWS Berechnung nach ID
    public Optional<SwsBerechnung> findById(int swsId) throws SQLException {
        String sql = "SELECT * FROM SWS_BERECHNUNG WHERE sws_berechnung = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, swsId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                SwsBerechnung sws = mapResultSet(rs);
                DatabaseConnection.closeResultSet(rs);
                return Optional.of(sws);
            }
            DatabaseConnection.closeResultSet(rs);
            return Optional.empty();
        }
    }

    // READ - Alle SWS Berechnungen
    public List<SwsBerechnung> findAll() throws SQLException {
        String sql = "SELECT * FROM SWS_BERECHNUNG";
        List<SwsBerechnung> sws = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                sws.add(mapResultSet(rs));
            }
            DatabaseConnection.closeResultSet(rs);
        }
        return sws;
    }

    // UPDATE - SWS Berechnung aktualisieren
    public boolean update(SwsBerechnung sws) throws SQLException {
        String sql = """
                UPDATE SWS_BERECHNUNG
                SET arbeit_id = ?, betreuer_id = ?, semester_id = ?, pruefungsordnung_id = ?, sws_wert = ?, rolle = ?, berechnet_am = ?
                WHERE sws_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sws.getArbeitId());
            pstmt.setInt(2, sws.getBetreuerId());
            pstmt.setInt(3, sws.getSemesterId());
            pstmt.setInt(4, sws.getPruefungsordnungId());
            pstmt.setFloat(5, sws.getSwsWert());
            pstmt.setString(6, sws.getRolle());
            pstmt.setDate(7, java.sql.Date.valueOf(sws.getBerechnetAm()));
            pstmt.setInt(8, sws.getSwsId());

            return pstmt.executeUpdate() > 0;
        }
    }

    // DELETE - SWS Berechnung löschen
    public boolean delete(int swsId) throws SQLException {
        String sql = "DELETE FROM SWS_BERECHNUNG WHERE sws_id = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, swsId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Hilfsmethode: ResultSet in Objekt umwandeln
    private SwsBerechnung mapResultSet(ResultSet rs) throws SQLException {
        SwsBerechnung sws = new SwsBerechnung();
        sws.setSwsId(rs.getInt("sws_id"));
        sws.setArbeitId(rs.getInt("arbeit_id"));
        sws.setBetreuerId(rs.getInt("betreuer_id"));
        sws.setSemesterId(rs.getInt("semester_id"));
        sws.setPruefungsordnungId(rs.getInt("pruefungsordnung_id"));
        sws.setSwsWert(rs.getFloat("sws_wert"));
        sws.setRolle(rs.getString("rolle"));
        sws.setBerechnetAm(rs.getDate("berechnet_am").toLocalDate());
        return sws;
    }
}
