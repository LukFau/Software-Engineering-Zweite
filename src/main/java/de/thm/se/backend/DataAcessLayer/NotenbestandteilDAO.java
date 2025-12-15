package de.thm.se.backend.DataAcessLayer;

import de.thm.se.backend.model.Notenbestandteil;
import de.thm.se.backend.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NotenbestandteilDAO {

    // CREATE - Neuen Notenbestandteil
    public int create(Notenbestandteil note) throws SQLException {
        String sql = """
                INSERT INTO NOTENBESTANDTEIL
                (arbeit_id, betreuer_id, rolle, note_arbeit, note_kolloquium, gewichtung)
                VALUES(?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, note.getArbeitId());
            pstmt.setInt(2, note.getBetreuerId());
            pstmt.setString(3, note.getRolle());
            pstmt.setDouble(4, note.getNoteArbeit());
            pstmt.setDouble(5, note.getNoteKolloquium());
            pstmt.setDouble(6, note.getGewichtung());

            pstmt.executeUpdate();

            try (ResultSet generateKeys = pstmt.getGeneratedKeys()) {
                if (generateKeys.next()) {
                    return generateKeys.getInt(1);
                }
                throw new SQLException("Erstellen fehlgeschlagen, keine ID erhalten");
            }
        }
    }

    // READ - Notenbestandteil nach ID Suchen
    public Optional<Notenbestandteil> findById(int id) throws SQLException {
        String sql = "SELECT * FROM NOTENBESTANDTEIL WHERE noten_id = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Notenbestandteil note = mapResultSet(rs);
                DatabaseConnection.closeResultSet(rs);
                return Optional.of(note);
            }
            DatabaseConnection.closeResultSet(rs);
            return Optional.empty();
        }
    }

    // READ - Alle Noten
    public List<Notenbestandteil> findAll() throws SQLException {
        String sql = "SELECT * FROM NOTENBESTANDTEIL WHERE note_id = ?";
        List<Notenbestandteil> noten = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
                Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                noten.add(mapResultSet(rs));
            }
            DatabaseConnection.closeResultSet(rs);
        }
        return noten;
    }

    // UPDATE - Notenbestandteil aktualisieren
    public boolean update(Notenbestandteil note) throws SQLException {
        String sql = """
                UPDATE NOTENBESTANDTEIL
                SET rolle = ?, note_Arbeit = ?, note_kolloquium = ?, gewichtung = ?
                WHERE noten_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, note.getRolle());
            pstmt.setDouble(2, note.getNoteArbeit());
            pstmt.setDouble(3, note.getNoteKolloquium());
            pstmt.setDouble(4, note.getGewichtung());
            pstmt.setInt(5, note.getNotenId());

            return pstmt.executeUpdate() > 0;
        }
    }

    // DELETE - Note löschen
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM NOTENBESTANDTEIL WHERE noten_id = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Hilfsmethode: ResultSet in Objetk umwandeln
    private Notenbestandteil mapResultSet(ResultSet rs) throws SQLException {
        Notenbestandteil note = new Notenbestandteil();
        note.setNotenId(rs.getInt("note_id"));
        note.setArbeitId(rs.getInt("arbeit_id"));
        note.setBetreuerId(rs.getInt("betreuer_id"));
        note.setRolle(rs.getString("rolle"));
        note.setNoteArbeit(rs.getDouble("note_arbeit"));
        note.setNoteKolloquium(rs.getDouble("note_kolloquium"));
        note.setGewichtung(rs.getDouble("gewichtung"));
        return note;
    }
}
