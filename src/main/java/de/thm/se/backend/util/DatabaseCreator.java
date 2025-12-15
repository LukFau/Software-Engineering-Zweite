package de.thm.se.backend.util;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseCreator {

    private static final String DB_URL = "jdbc:sqlite:profis.db";

    public void createDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = ON;");

            // Reihenfolge beachten wegen Foreign Keys!
            createFachbereichTable(stmt); // NEU: Fachbereich muss vor Studiengang existieren
            createStudiengangTable(stmt);
            createStudierendeTable(stmt);
            createPruefungsordnungTable(stmt);
            createSemesterzeitTable(stmt);
            createSemesterTable(stmt);
            createWissenschaftlicheArbeitTable(stmt);
            createZeitdatenTable(stmt);
            createBetreuerTable(stmt);
            createNotenbestandteilTable(stmt);
            createSwsBerechnungTable(stmt);
            createExterneDatenquelleTable(stmt);
            createImportvorgangTable(stmt);

            System.out.println("Alle Tabellen wurden erfolgreich erstellt.");
        }
    }

    // NEU: Methode für Fachbereich
    private static void createFachbereichTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS FACHBEREICH (
                fachbereich_id INTEGER PRIMARY KEY AUTOINCREMENT,
                bezeichnung TEXT,
                fbname TEXT
            );
            """;
        stmt.execute(sql);
        System.out.println("Tabelle FACHBEREICH erstellt.");
    }

    private static void createStudiengangTable(Statement stmt) throws SQLException {
        // KORRIGIERT: fachbereich_id hinzugefügt
        String sql = """
            CREATE TABLE IF NOT EXISTS STUDIENGANG (
                studiengang_id INTEGER PRIMARY KEY AUTOINCREMENT,
                fachbereich_id INTEGER NOT NULL, 
                bezeichnung TEXT UNIQUE NOT NULL,
                kuerzel TEXT,
                abschlusstitel TEXT,
                abschluss TEXT,
                aktiv INTEGER DEFAULT 1,
                FOREIGN KEY (fachbereich_id) REFERENCES FACHBEREICH(fachbereich_id)
            );
            """;
        stmt.execute(sql);
        System.out.println("Tabelle STUDIENGANG erstellt.");
    }

    // ... (Restliche Methoden bleiben gleich, hier der Vollständigkeit halber aufgeführt)

    private static void createStudierendeTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS STUDIERENDE (
                studierenden_id INTEGER PRIMARY KEY AUTOINCREMENT,
                matrikelnummer TEXT UNIQUE NOT NULL,
                vorname TEXT NOT NULL,
                nachname TEXT NOT NULL,
                email TEXT,
                geburtsdatum DATE,
                adresse TEXT
            );
            """;
        stmt.execute(sql);
        System.out.println("Tabelle STUDIERENDE erstellt.");
    }

    private static void createPruefungsordnungTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS PRUEFUNGSORDNUNG (
                po_id INTEGER PRIMARY KEY AUTOINCREMENT,
                studiengang_id INTEGER NOT NULL,
                bezeichnung TEXT NOT NULL,
                gueltig_ab DATE,
                gueltig_bis DATE,
                sws_referent INTEGER,
                sws_koreferent INTEGER,
                FOREIGN KEY (studiengang_id) REFERENCES STUDIENGANG(studiengang_id)
            );
            """;
        stmt.execute(sql);
        System.out.println("Tabelle PRUEFUNGSORDNUNG erstellt.");
    }

    private static void createSemesterzeitTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS SEMESTERZEIT (
                semesterzeit_id INTEGER PRIMARY KEY AUTOINCREMENT,
                beginn DATE NOT NULL,
                ende DATE NOT NULL,
                bezeichnung TEXT
            );
            """;
        stmt.execute(sql);
        System.out.println("Tabelle SEMESTERZEIT erstellt.");
    }

    private static void createSemesterTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS SEMESTER (
                semester_id INTEGER PRIMARY KEY AUTOINCREMENT,
                semesterzeit_id INTEGER NOT NULL,
                bezeichnung TEXT UNIQUE NOT NULL,
                typ TEXT,
                jahr INTEGER,
                FOREIGN KEY (semesterzeit_id) REFERENCES SEMESTERZEIT(semesterzeit_id)
            );
            """;
        stmt.execute(sql);
        System.out.println("Tabelle SEMESTER erstellt.");
    }

    private static void createWissenschaftlicheArbeitTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS WISSENSCHAFTLICHE_ARBEIT (
                arbeit_id INTEGER PRIMARY KEY AUTOINCREMENT,
                studierenden_id INTEGER NOT NULL,
                studiengang_id INTEGER NOT NULL,
                pruefungsordnung_id INTEGER NOT NULL,
                semester_id INTEGER NOT NULL,
                titel TEXT NOT NULL,
                typ TEXT,
                status TEXT,
                FOREIGN KEY (studierenden_id) REFERENCES STUDIERENDE(studierenden_id),
                FOREIGN KEY (studiengang_id) REFERENCES STUDIENGANG(studiengang_id),
                FOREIGN KEY (pruefungsordnung_id) REFERENCES PRUEFUNGSORDNUNG(po_id),
                FOREIGN KEY (semester_id) REFERENCES SEMESTER(semester_id)
            );
            """;
        stmt.execute(sql);
        System.out.println("Tabelle WISSENSCHAFTLICHE_ARBEIT erstellt.");
    }

    private static void createZeitdatenTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS ZEITDATEN (
                zeit_id INTEGER PRIMARY KEY AUTOINCREMENT,
                arbeit_id INTEGER NOT NULL UNIQUE,
                anfangsdatum DATE,
                abgabedatum DATE,
                kolloquiumsdatum DATE,
                FOREIGN KEY (arbeit_id) REFERENCES WISSENSCHAFTLICHE_ARBEIT(arbeit_id)
            );
            """;
        stmt.execute(sql);
        System.out.println("Tabelle ZEITDATEN erstellt.");
    }

    private static void createBetreuerTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS BETREUER (
                betreuer_id INTEGER PRIMARY KEY AUTOINCREMENT,
                vorname TEXT NOT NULL,
                nachname TEXT NOT NULL,
                titel TEXT,
                email TEXT,
                rolle TEXT
            );
            """;
        stmt.execute(sql);
        System.out.println("Tabelle BETREUER erstellt.");
    }

    private static void createNotenbestandteilTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS NOTENBESTANDTEIL (
                noten_id INTEGER PRIMARY KEY AUTOINCREMENT,
                arbeit_id INTEGER NOT NULL,
                betreuer_id INTEGER NOT NULL,
                rolle TEXT,
                note_arbeit REAL,
                note_kolloquium REAL,
                gewichtung REAL,
                FOREIGN KEY (arbeit_id) REFERENCES WISSENSCHAFTLICHE_ARBEIT(arbeit_id),
                FOREIGN KEY (betreuer_id) REFERENCES BETREUER(betreuer_id)
            );
            """;
        stmt.execute(sql);
        System.out.println("Tabelle NOTENBESTANDTEIL erstellt.");
    }

    private static void createSwsBerechnungTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS SWS_BERECHNUNG (
                sws_id INTEGER PRIMARY KEY AUTOINCREMENT,
                arbeit_id INTEGER NOT NULL,
                betreuer_id INTEGER NOT NULL,
                semester_id INTEGER NOT NULL,
                pruefungsordnung_id INTEGER NOT NULL,
                sws_wert REAL,
                rolle TEXT,
                berechnet_am DATE,
                FOREIGN KEY (arbeit_id) REFERENCES WISSENSCHAFTLICHE_ARBEIT(arbeit_id),
                FOREIGN KEY (betreuer_id) REFERENCES BETREUER(betreuer_id),
                FOREIGN KEY (semester_id) REFERENCES SEMESTER(semester_id),
                FOREIGN KEY (pruefungsordnung_id) REFERENCES PRUEFUNGSORDNUNG(po_id)
            );
            """;
        stmt.execute(sql);
        System.out.println("Tabelle SWS_BERECHNUNG erstellt.");
    }

    private static void createExterneDatenquelleTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS EXTERNE_DATENQUELLE (
                quelle_id INTEGER PRIMARY KEY AUTOINCREMENT,
                bezeichnung TEXT NOT NULL,
                quelltyp TEXT,
                pfad_url TEXT,
                format TEXT,
                aktiv INTEGER DEFAULT 1
            );
            """;
        stmt.execute(sql);
        System.out.println("Tabelle EXTERNE_DATENQUELLE erstellt.");
    }

    private static void createImportvorgangTable(Statement stmt) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS IMPORTVORGANG (
                import_id INTEGER PRIMARY KEY AUTOINCREMENT,
                quelle_id INTEGER NOT NULL,
                import_zeitpunkt DATETIME,
                status TEXT,
                anzahl_datensaetze INTEGER,
                fehlermeldung TEXT,
                FOREIGN KEY (quelle_id) REFERENCES EXTERNE_DATENQUELLE(quelle_id)
            );
            """;
        stmt.execute(sql);
        System.out.println("Tabelle IMPORTVORGANG erstellt.");
    }
}