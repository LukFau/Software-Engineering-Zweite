package de.thm.se.backend.DataAcessLayer;

import de.thm.se.backend.model.WissenschaftlicheArbeit;
import de.thm.se.backend.model.ArbeitMitDetails;
import de.thm.se.backend.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WissenschaftlicheArbeitDAO {

    // CREATE - Neue Arbeit anlegen
    public int create(WissenschaftlicheArbeit arbeit) throws SQLException {
        String sql = """
                INSERT INTO WISSENSCHAFTLICHE_ARBEIT
                (studierenden_id, studiengang_id, pruefungsordnung_id, semester_id, titel, typ, status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        // try with ressources schließt connection und preparedStatement nach Abschluss
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, arbeit.getStudierendenId());
            pstmt.setInt(2, arbeit.getStudiengangId());
            pstmt.setInt(3, arbeit.getPruefungsordnungId());
            pstmt.setInt(4, arbeit.getSemesterId());
            pstmt.setString(5, arbeit.getTitel());
            pstmt.setString(6, arbeit.getTyp());
            pstmt.setString(7, arbeit.getStatus());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                throw new SQLException("Erstellen fehlgeschlagen, keine ID erhalten.");
            }
        }
    }

    // READ - Arbeit nach ID Suchen
    public Optional<WissenschaftlicheArbeit> findById(int wissId) throws SQLException {
        String sql = "SELECT * FROM WISSENSCHAFTLICHE_ARBEIT WHERE arbeit_id = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, wissId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                WissenschaftlicheArbeit arbeit = mapResultSet(rs);
                DatabaseConnection.closeResultSet(rs);// ResultSet nach verarbeitung schließen
                return Optional.of(arbeit);
            }
            DatabaseConnection.closeResultSet(rs);
            return Optional.empty();
        }
    }

    //READ - Alle Arbeiten
    public List<WissenschaftlicheArbeit> findAll() throws SQLException {
        String sql = "SELECT * FROM WISSENSCHAFTLICHE_ARBEIT";
        List<WissenschaftlicheArbeit> arbeiten = new ArrayList<>();

        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    arbeiten.add(mapResultSet(rs));
                }
                DatabaseConnection.closeResultSet(rs);
            }
        return arbeiten;
    }

    // READ - Alle Arbeiten eines Studierenden
    public List<WissenschaftlicheArbeit> findByStudierendenId(int studierendenId) throws SQLException {
        String sql = "SELECT * FROM WISSENSCHAFTLICHE_ARBEIT WHERE studierenden_id = ?";
        List<WissenschaftlicheArbeit> arbeiten = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studierendenId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                arbeiten.add(mapResultSet(rs));
            }
            DatabaseConnection.closeResultSet(rs);
        }
        return arbeiten;
    }

    // READ - Arbeit nach Status suchen
    public List<WissenschaftlicheArbeit> findByStatus(String status) throws SQLException {
        String sql = "SELECT * FROM WISSENSCHAFTLICHE_ARBEIT WHERE status = ?";
        List<WissenschaftlicheArbeit> arbeiten = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                arbeiten.add(mapResultSet(rs));
            }
            DatabaseConnection.closeResultSet(rs);
        }
        return arbeiten;
    }

    // READ - Arbeiten mit vollständigen Informationen (mit JOINs)
    public List<ArbeitMitDetails> findAlleMitDetails() throws SQLException {
        String sql = """
                SELECT
                    w.arbeit_id,
                    w.titel,
                    w.typ,
                    w.status,
                    s.vorname || ' ' || s.nachname AS studierender_name,
                    st.bezeichnung AS studiengang,
                    sem.bezeichnung AS semester
                FROM WISSENSCHAFTLICHE_ARBEIT w
                JOIN STUDIERENDE s ON w.studierenden_id = s.studierenden_id
                JOIN STUDIENGANG st ON w.studiengang_id = st.studiengang_id
                JOIN SEMESTER sem ON w.semester_id = sem.semester_id
                ORDER BY w.arbeit_id DESC
                """;

        List<ArbeitMitDetails> arbeiten = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ArbeitMitDetails arbeit = new ArbeitMitDetails();
                arbeit.setArbeitId(rs.getInt("arbeit_id"));
                arbeit.setTitel(rs.getString("titel"));
                arbeit.setTyp(rs.getString("typ"));
                arbeit.setStatus(rs.getString("status"));
                arbeit.setStudierenderName(rs.getString("studierender_name"));
                arbeit.setStudiengang(rs.getString("studiengang"));
                arbeit.setSemester(rs.getString("semester"));
                arbeiten.add(arbeit);
            }
        }
        return arbeiten;
    }

    // UPDATE - Arbeit aktualisieren
    public boolean update(WissenschaftlicheArbeit arbeit) throws SQLException {
        String sql = """
                UPDATE WISSENSCHAFTLICHE_ARBEIT
                SET titel = ?, typ = ?, status = ?
                WHERE arbeit_id = ?
                """;

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, arbeit.getTitel());
            pstmt.setString(2, arbeit.getTyp());
            pstmt.setString(3, arbeit.getStatus());
            pstmt.setInt(4, arbeit.getArbeitId());

            return pstmt.executeUpdate() > 0;
        }
    }

    // UPDATE - Nur Status ändern (häufige Operation)
    public boolean updateStatus(int arbeitId, String neuerStatus) throws SQLException {
        String sql = "UPDATE WISSENSCHAFTLICHE_ARBEIT SET status = ? WHERE arbeit_id = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, neuerStatus);
            pstmt.setInt(2, arbeitId);

            return pstmt.executeUpdate() > 0;
        }
    }

    // DELETE - Arbeit löschen
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM WISSENSCHAFTLICHE_ARBEIT WHERE arbeit_id = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Hilfsmethode: ResultSet in Objekt umwandeln
    private WissenschaftlicheArbeit mapResultSet(ResultSet rs) throws SQLException {
        WissenschaftlicheArbeit arbeit = new WissenschaftlicheArbeit();
        arbeit.setArbeitId(rs.getInt("arbeit_id"));
        arbeit.setStudierendenId(rs.getInt("studierenden_id"));
        arbeit.setStudiengangId(rs.getInt("studiengang_id"));
        arbeit.setPruefungsordnungId(rs.getInt("pruefungsordnung_id"));
        arbeit.setSemesterId(rs.getInt("semester_id"));
        arbeit.setTitel(rs.getString("titel"));
        arbeit.setTyp(rs.getString("typ"));
        arbeit.setStatus(rs.getString("status"));
        return arbeit;
    }
}