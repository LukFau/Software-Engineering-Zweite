package de.thm.se.backend.util;

import java.sql.*;

/**
 * Utility-Klasse für die zentrale Verwaltung von Datenbankverbindungen.
 * Diese Klasse kapselt alle Operationen zum Öffnen und Schließen von
 * Datenbankverbindungen sowie ResultSets und Statements.
 * 
 * Durch die Verwendung dieser Klasse wird Code-Duplikation vermieden
 * und die Datenbankverbindung zentral verwaltet.
 */
public class DatabaseConnection {

    // Datenbankverbindungs-URL - SQLite Beispiel (anpassen nach Bedarf)
    private static final String DB_URL = "jdbc:sqlite:profis.db";
    
    /**
     * Öffnet eine neue Datenbankverbindung.
     * 
     * Diese Methode:
     * 1. Erstellt eine neue Connection zur Datenbank via DriverManager
     * 2. Erstellt ein Statement um SQL-Befehle auszuführen
     * 3. Aktiviert Foreign Key Constraints für referenzielle Integrität
     *    (PRAGMA foreign_keys = ON - SQLite spezifisch)
     * 4. Gibt die Connection zurück
     * 
     * @return Connection - Eine offene Datenbankverbindung
     * @throws SQLException wenn die Verbindung fehlschlägt oder SQL-Fehler auftreten
     */
    public static Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        stmt.execute("PRAGMA foreign_keys = ON");
        stmt.close(); // Statement sofort nach Gebrauch schließen
        return conn;
    }
    
    /**
     * Schließt eine Datenbankverbindung sicher.
     * 
     * Diese Methode prüft zunächst, ob die Connection nicht null ist,
     * und schließt sie dann. Falls ein Fehler auftritt, wird dieser
     * abgefangen und als Fehlermeldung ausgegeben (nicht geworfen).
     * 
     * @param connection Die zu schließende Connection
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Fehler beim Schließen der Datenbankverbindung: " + e.getMessage());
            }
        }
    }
    
    /**
     * Schließt ein PreparedStatement oder Statement sicher.
     * 
     * PreparedStatements werden verwendet für parameterisierte Abfragen
     * und sollten nach Gebrauch geschlossen werden um Ressourcen freizugeben.
     * 
     * @param statement Das zu schließende Statement/PreparedStatement
     */
    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.err.println("Fehler beim Schließen des Statements: " + e.getMessage());
            }
        }
    }
    
    /**
     * Schließt ein ResultSet sicher.
     * 
     * Ein ResultSet enthält die Ergebnisse einer SQL-Abfrage.
     * ResultSets sollten nach der Verarbeitung geschlossen werden,
     * um Datenbankressourcen freizugeben.
     * 
     * @param resultSet Das zu schließende ResultSet
     */
    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.err.println("Fehler beim Schließen des ResultSets: " + e.getMessage());
            }
        }
    }
}
