package de.thm.se.backend.DataAcessLayer;

import de.thm.se.backend.model.Studierende;
import de.thm.se.backend.util.DatabaseConnection;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StudierendeDAO {

    public int create(Studierende studi) throws SQLException {
        String sql = """
                INSERT INTO STUDIERENDE
                (matrikelnummer, vorname, nachname, email, geburtsdatum, adresse)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studi.getMatrikelnummer());
            pstmt.setString(2, studi.getVorname());
            pstmt.setString(3, studi.getNachname());
            pstmt.setString(4, studi.getEmail());
            if (studi.getGeburtsdatum() != null) {
                pstmt.setDate(5, java.sql.Date.valueOf(studi.getGeburtsdatum()));
            } else {
                pstmt.setNull(5, Types.DATE);
            }
            pstmt.setString(6, studi.getAdresse());
            pstmt.executeUpdate();

            // WORKAROUND
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new SQLException("Erstellen fehlgeschlagen, keine ID erhalten");
            }
        }
    }

    public Optional<Studierende> findById(int studiId) throws SQLException {
        String sql = "SELECT * FROM STUDIERENDE WHERE studierenden_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studiId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Studierende studi = mapResultSet(rs);
                DatabaseConnection.closeResultSet(rs);
                return Optional.of(studi);
            }
            DatabaseConnection.closeResultSet(rs);
            return Optional.empty();
        }
    }

    public List<Studierende> findAll() throws SQLException {
        String sql = "SELECT * FROM STUDIERENDE";
        List<Studierende> studi = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                studi.add(mapResultSet(rs));
            }
            DatabaseConnection.closeResultSet(rs);
        }
        return studi;
    }

    public Optional<Studierende> findByMatrikelnummer(String matrikelnummer) throws SQLException {
        String sql = "SELECT * FROM STUDIERENDE WHERE matrikelnummer = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matrikelnummer);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Studierende studi = mapResultSet(rs);
                DatabaseConnection.closeResultSet(rs);
                return Optional.of(studi);
            }
            DatabaseConnection.closeResultSet(rs);
            return Optional.empty();
        }
    }

    public boolean update(Studierende studi) throws SQLException {
        String sql = """
                UPDATE STUDIERENDE
                SET matrikelnummer = ?, vorname = ?, nachname = ?, email = ?, geburtsdatum = ?, adresse = ?
                WHERE studierenden_id = ?
                """;
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studi.getMatrikelnummer());
            pstmt.setString(2, studi.getVorname());
            pstmt.setString(3, studi.getNachname());
            pstmt.setString(4, studi.getEmail());
            if (studi.getGeburtsdatum() != null) {
                pstmt.setDate(5, java.sql.Date.valueOf(studi.getGeburtsdatum()));
            } else {
                pstmt.setNull(5, Types.DATE);
            }
            pstmt.setString(6, studi.getAdresse());
            pstmt.setInt(7, studi.getStudierendenId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean delete(int studierendenId) throws SQLException {
        String sql = "DELETE FROM STUDIERENDE WHERE studierenden_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studierendenId);
            return pstmt.executeUpdate() > 0;
        }
    }

    private Studierende mapResultSet(ResultSet rs) throws SQLException {
        Studierende studi = new Studierende();
        studi.setStudierendenId(rs.getInt("studierenden_id"));
        studi.setMatrikelnummer(rs.getString("matrikelnummer"));
        studi.setVorname(rs.getString("vorname"));
        studi.setNachname(rs.getString("nachname"));
        studi.setEmail(rs.getString("email"));
        Date gebDatum = rs.getDate("geburtsdatum");
        if (gebDatum != null) {
            studi.setGeburtsdatum(gebDatum.toLocalDate());
        }
        studi.setAdresse(rs.getString("adresse"));
        return studi;
    }
}