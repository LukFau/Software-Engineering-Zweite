package de.thm.se.backend.DataAcessLayer;

import de.thm.se.backend.model.Pruefungsordnung;
import de.thm.se.backend.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PruefungsordnungDAO {

    //CREATE - Neue Prüfungsordnung
    public int create(Pruefungsordnung pro) throws SQLException {
        String sql = """
                INSERT INTO PRUEFUNGSORDNUNG
                (studiengang_id, bezeichnung, gueltig_ab, gueltig_bis, sws_referent, sws_koreferent)
                VALUES(?, ?, ?, ?, ?, ?)
                """;
        
        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

                pstmt.setInt(1, pro.getStudiengangId());
                pstmt.setString(2, pro.getBezeichnung());
                pstmt.setDate(3, java.sql.Date.valueOf(pro.getGueltigAb()));
                pstmt.setDate(4, java.sql.Date.valueOf(pro.getGueltigBis()));
                pstmt.setInt(5, pro.getSwsReferent());
                pstmt.setInt(6, pro.getSwsKoreferent());

                pstmt.executeUpdate();

                try(ResultSet generatedKeys = pstmt.getGeneratedKeys()){
                    if(generatedKeys.next()){
                        return generatedKeys.getInt(1);
                    }
                    throw new SQLException("Erstellen fehlgeschlagen, keine ID erhalten.");
                }
            }
    }

    //READ - Prüfungsordnung nach ID
    public Optional<Pruefungsordnung> findById(int id) throws SQLException {
        String sql = "SELECT * FROM PRUEFUNGSORDNUNG WHERE pruefungs_id";

        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                if(rs.next()) {
                    Pruefungsordnung pro = mapResultSet(rs);
                    DatabaseConnection.closeResultSet(rs);
                    return Optional.of(pro);
                }
                DatabaseConnection.closeResultSet(rs);
                return Optional.empty();
            }
    }

    //READ - Alle Prüfungsordnungen
    public List<Pruefungsordnung> findAll() throws SQLException {
        String sql = "SELECT * FROM PRUEFUNGSORDNUNG";
        List<Pruefungsordnung> pro = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                pro.add(mapResultSet(rs));
            }
            DatabaseConnection.closeResultSet(rs);
        }
        return pro;
    }
    
    //UPDATE - Prüfungsordnung aktualisieren
    public boolean update(Pruefungsordnung pro) throws SQLException {
        String sql = """
                UPDATE PRUEFUNGSORDNUNG
                SET bezeichnung = ?, gueltig_ab = ?, gueltig_bis = ?, sws_referent = ?, sws_koreferent = ?
                WHERE pruefungsordnung_id = ?
                """;
        
        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

                pstmt.setString(1, pro.getBezeichnung());
                pstmt.setDate(2, java.sql.Date.valueOf(pro.getGueltigAb()));
                pstmt.setDate(3, java.sql.Date.valueOf(pro.getGueltigBis()));
                pstmt.setInt(4, pro.getSwsReferent());
                pstmt.setInt(5, pro.getSwsKoreferent());
                pstmt.setInt(6, pro.getPruefungsordnungId());

                return pstmt.executeUpdate() > 0;
            }
    }

    //DELETE - Prüfungsordnung löschen
    public boolean delete(int proId) throws SQLException { 
        String sql = "DELETE FROM PRUEFUNGSORDNUNG WHERE pruefungsordnung_id = ?";

        try(Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

                pstmt.setInt(1, proId);
                return pstmt.executeUpdate() > 0;
            }
    }
    
    //Hilfsmethoden: ResultSet in Objekt umwandeln
    private Pruefungsordnung mapResultSet(ResultSet rs) throws SQLException {
        Pruefungsordnung pro = new Pruefungsordnung();
        pro.setPruefungsordnungId(rs.getInt("pruefungsordnung_id"));
        pro.setStudiengangId(rs.getInt("studiengang_id"));
        pro.setBezeichnung(rs.getString("bezeichnung"));
        pro.setGueltigAb(rs.getDate("gueltig_ab").toLocalDate());
        pro.setGueltigBis(rs.getDate("gueltig_bis").toLocalDate());
        pro.setSwsReferent(rs.getInt("sws_referent"));
        pro.setSwsKoreferent(rs.getInt("sws_koreferent"));
        return pro;
    }
}   