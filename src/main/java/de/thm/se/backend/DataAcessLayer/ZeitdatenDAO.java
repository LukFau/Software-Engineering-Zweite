package de.thm.se.backend.DataAcessLayer;

import de.thm.se.backend.model.Zeitdaten;
import de.thm.se.backend.util.DatabaseConnection;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ZeitdatenDAO {

    public int create(Zeitdaten zeit) throws SQLException {
        String sql = """
                INSERT INTO ZEITDATEN
                (arbeit_id, anfangsdatum, abgabedatum, kolloquiumsdatum)
                VALUES(?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, zeit.getArbeitId());
            pstmt.setDate(2, zeit.getAnfangsdatum() != null ? java.sql.Date.valueOf(zeit.getAnfangsdatum()) : null);
            pstmt.setDate(3, zeit.getAbgabedatum() != null ? java.sql.Date.valueOf(zeit.getAbgabedatum()) : null);
            pstmt.setDate(4, zeit.getKolloquiumsdatum() != null ? java.sql.Date.valueOf(zeit.getKolloquiumsdatum()) : null);
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

    public boolean update(Zeitdaten zeit) throws SQLException {
        String sql = """
                UPDATE ZEITDATEN
                SET arbeit_id = ?, anfangsdatum = ?, abgabedatum = ?, kolloquiumsdatum = ?
                WHERE zeit_id = ?
                """;
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, zeit.getArbeitId());
            pstmt.setDate(2, zeit.getAnfangsdatum() != null ? java.sql.Date.valueOf(zeit.getAnfangsdatum()) : null);
            pstmt.setDate(3, zeit.getAbgabedatum() != null ? java.sql.Date.valueOf(zeit.getAbgabedatum()) : null);
            pstmt.setDate(4, zeit.getKolloquiumsdatum() != null ? java.sql.Date.valueOf(zeit.getKolloquiumsdatum()) : null);
            pstmt.setInt(5, zeit.getZeitId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int zeitId) throws SQLException {
        String sql = "DELETE FROM ZEITDATEN WHERE zeit_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, zeitId);
            return pstmt.executeUpdate() > 0;
        }
    }

    private Zeitdaten mapResultSet(ResultSet rs) throws SQLException {
        Zeitdaten zeit = new Zeitdaten();
        zeit.setZeitId(rs.getInt("zeit_id"));
        zeit.setArbeitId(rs.getInt("arbeit_id"));
        Date start = rs.getDate("anfangsdatum");
        if(start != null) zeit.setAnfangsdatum(start.toLocalDate());
        Date abgabe = rs.getDate("abgabedatum");
        if(abgabe != null) zeit.setAbgabedatum(abgabe.toLocalDate());
        Date koll = rs.getDate("kolloquiumsdatum");
        if(koll != null) zeit.setKolloquiumsdatum(koll.toLocalDate());
        return zeit;
    }
}