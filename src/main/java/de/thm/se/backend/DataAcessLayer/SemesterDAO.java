package de.thm.se.backend.DataAcessLayer;

import de.thm.se.backend.model.Semester;
import de.thm.se.backend.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SemesterDAO {
    
    //CREATE - Neues Semester
    public int create(Semester sem) throws SQLException {
        String sql = """
                INSERT INTO SEMESTER
                (semesterzeit_id, bezeichnung, typ, jahr)
                VALUES(?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

                pstmt.setInt(1, sem.getSemesterzeitId());
                pstmt.setString(2, sem.getBezeichnung());
                pstmt.setString(3, sem.getTyp());
                pstmt.setDate(4, java.sql.Date.valueOf(sem.getJahr()));
                pstmt.executeUpdate();

                try (ResultSet generatedKey = pstmt.getGeneratedKeys()){
                    if(generatedKey.next()){
                        return generatedKey.getInt(1);
                    }
                    throw new SQLException("Erstellen fehlgeschlagen, keine ID erhalten.");
                }
            }
    }

    //READ - Semester nach ID
    public Optional<Semester> findById(int semesterId) throws SQLException {
        String sql = "SELECT * FROM SEMESTER WHERE semester_id";

        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
                
                pstmt.setInt(1, semesterId);
                ResultSet rs = pstmt.executeQuery();
                if(rs.next()){
                    Semester sem = mapResultSet(rs);
                    DatabaseConnection.closeResultSet(rs);
                    return Optional.of(sem);
                }
                DatabaseConnection.closeResultSet(rs);
                return Optional.empty();

            }
    }

    //READ - Alle Semester
    public List<Semester> findAll() throws SQLException {
        String sql = "SELECT * FROM SEMESTER";
        List<Semester> sem = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    sem.add(mapResultSet(rs));
                }
                DatabaseConnection.closeResultSet(rs);
            }
            return sem;
    }

    //UPDATE - Semester aktualisieren
    public boolean update(Semester sem) throws SQLException {
        String sql = """
                UPDATE SEMESTER
                SET semesterzeit_id = ?, bezeichnung = ?, typ = ?, jahr = ?
                WHERE semester_id = ?
                """;
        
        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

                pstmt.setInt(1, sem.getSemesterzeitId());
                pstmt.setString(2, sem.getBezeichnung());
                pstmt.setString(3, sem.getTyp());
                pstmt.setDate(4, java.sql.Date.valueOf(sem.getJahr()));
                pstmt.setInt(5, sem.getSemesterId());

                return pstmt.executeUpdate() > 0;
            }
    }

    //DELETE - Semester löschen
    public boolean delete(int semId) throws SQLException {
        String sql = "DELETE FROM SEMESTER WHERE semester_id = ?";

        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

                pstmt.setInt(1, semId);
                return pstmt.executeUpdate() > 0;
            }
    }

    //Hilfsmethode: ResultSet in Objekt umwandeln 
    private Semester mapResultSet(ResultSet rs) throws SQLException {
        Semester sem = new Semester();
        sem.setSemesterId(rs.getInt("semester_id"));
        sem.setSemesterzeitId(rs.getInt("semesterzeit_id"));
        sem.setBezeichnung(rs.getString("bezeichnung"));
        sem.setTyp(rs.getString("typ"));
        sem.setJahr(rs.getDate("jahr").toLocalDate());
        return sem;
    }
}
