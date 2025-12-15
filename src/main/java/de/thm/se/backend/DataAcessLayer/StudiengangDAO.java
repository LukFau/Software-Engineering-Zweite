package de.thm.se.backend.DataAcessLayer;

import de.thm.se.backend.model.Studiengang;
import de.thm.se.backend.util.DatabaseConnection;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StudiengangDAO {
    // CREATE - Neuen Studiengang anlegen
    public int create(Studiengang sGang) throws SQLException {
        String sql = """
                INSERT INTO STUDIENGANG
                (fachbereich_id, bezeichnung, kuerzel, abschlusstitel, abschluss, aktiv)
                VALUES(?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sGang.getFachbereichId());
            pstmt.setString(2, sGang.getBezeichnung());
            pstmt.setString(3, sGang.getKuerzel());
            pstmt.setString(4, sGang.getAbschlusstitel());
            pstmt.setString(5, sGang.getAbschluss());
            pstmt.setBoolean(6, sGang.isAktiv());
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

    public Optional<Studiengang> findById(int sGangid) throws SQLException {
        String sql = "SELECT * FROM STUDIENGANG WHERE studiengang_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, sGangid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Studiengang sg = mapResultSet(rs);
                DatabaseConnection.closeResultSet(rs);
                return Optional.of(sg);
            }
            DatabaseConnection.closeResultSet(rs);
            return Optional.empty();
        }
    }

    public List<Studiengang> findAll() throws SQLException {
        String sql = "SELECT * FROM STUDIENGANG";
        List<Studiengang> studiengaenge = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                studiengaenge.add(mapResultSet(rs));
            }
            DatabaseConnection.closeResultSet(rs);
        }
        return studiengaenge;
    }

    public boolean update(Studiengang studiengang) throws SQLException {
        String sql = """
                UPDATE STUDIENGANG
                SET fachbereich_id = ?, bezeichnung = ?, kuerzel = ?, abschlusstitel = ?, abschluss = ?, aktiv = ?
                WHERE studiengang_id = ?
                """;
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studiengang.getFachbereichId());
            pstmt.setString(2, studiengang.getBezeichnung());
            pstmt.setString(3, studiengang.getKuerzel());
            pstmt.setString(4, studiengang.getAbschlusstitel());
            pstmt.setString(5, studiengang.getAbschluss());
            pstmt.setBoolean(6, studiengang.isAktiv());
            pstmt.setInt(7, studiengang.getStudiengangId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int studiengangId) throws SQLException {
        String sql = "DELETE FROM STUDIENGANG WHERE studiengang_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studiengangId);
            return pstmt.executeUpdate() > 0;
        }
    }

    private Studiengang mapResultSet(ResultSet rs) throws SQLException {
        Studiengang studiengang = new Studiengang();
        studiengang.setStudiengangId(rs.getInt("studiengang_id"));
        studiengang.setFachbereichId(rs.getInt("fachbereich_id"));
        studiengang.setBezeichnung(rs.getString("bezeichnung"));
        studiengang.setKuerzel(rs.getString("kuerzel"));
        studiengang.setAbschlusstitel(rs.getString("abschlusstitel"));
        studiengang.setAbschluss(rs.getString("abschluss"));
        studiengang.setAktiv(rs.getBoolean("aktiv"));
        return studiengang;
    }
}