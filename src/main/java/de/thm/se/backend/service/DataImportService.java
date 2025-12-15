package de.thm.se.backend.service;

import de.thm.se.backend.DataAcessLayer.*;
import de.thm.se.backend.model.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class DataImportService {

    @Autowired private StudierendeDAO studierendeDAO;
    @Autowired private WissenschaftlicheArbeitDAO arbeitDAO;
    @Autowired private ZeitdatenDAO zeitdatenDAO;
    @Autowired private NotenbestandteilDAO notenbestandteilDAO;
    @Autowired private BetreuerDAO betreuerDAO;
    @Autowired private StudiengangDAO studiengangDAO;
    @Autowired private FachbereichDAO fachbereichDAO;
    @Autowired private PruefungsordnungDAO poDAO;
    @Autowired private SemesterDAO semesterDAO;
    @Autowired private SemesterzeitDAO semesterzeitDAO;

    // Cache für Betreuer, um Duplikate während des Imports zu vermeiden
    private final Map<String, Integer> betreuerCache = new HashMap<>();

    public String importData(InputStream inputStream) {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheet("Arbeiten");
            if (sheet == null) sheet = workbook.getSheetAt(0);

            // 1. Basis-Daten sicherstellen (Studiengang, Semester, etc.)
            // Damit die Foreign Keys nicht fehlschlagen
            int defaultFachbereichId = getOrCreateDefaultFachbereich();
            int defaultStudiengangId = getOrCreateDefaultStudiengang(defaultFachbereichId);
            int defaultPoId = getOrCreateDefaultPO(defaultStudiengangId);
            int defaultSemesterId = getOrCreateDefaultSemester();

            int importCount = 0;

            // Start bei Zeile 1 (Header überspringen)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Daten aus Excel lesen
                String status = getCellVal(row, 0);       // A: Status
                // B: Deputat (ignorieren wir hier)
                String email = getCellVal(row, 2);        // C: Kontakt
                String erstprueferName = getCellVal(row, 3); // D: Erstprüfer
                String zweitprueferName = getCellVal(row, 4); // E: Zweitprüfer
                String nachname = getCellVal(row, 5);     // F: Nachname
                String vorname = getCellVal(row, 6);      // G: Vorname
                // H: Anrede
                String matrikelnr = getCellVal(row, 8);   // I: Matrikelnr
                // J: Fachbereich ID (könnten wir nutzen, hier vereinfacht default)
                // K: Studiengang Name (könnten wir nutzen, hier vereinfacht default)
                String titel = getCellVal(row, 11);       // L: Titel

                LocalDate startDatum = getDateVal(row, 12); // M: Start
                LocalDate abgabeDatum = getDateVal(row, 13); // N: Abgabe
                LocalDate kollDatum = getDateVal(row, 15);   // P: Kolloquium

                if (titel.isEmpty() || matrikelnr.isEmpty()) continue;

                // 2. Studierenden verarbeiten
                int studentId = getOrCreateStudent(matrikelnr, vorname, nachname, email);

                // 3. Arbeit anlegen
                WissenschaftlicheArbeit arbeit = new WissenschaftlicheArbeit();
                arbeit.setStudierendenId(studentId);
                arbeit.setStudiengangId(defaultStudiengangId);
                arbeit.setPruefungsordnungId(defaultPoId);
                arbeit.setSemesterId(defaultSemesterId);
                arbeit.setTitel(titel);
                arbeit.setTyp("Bachelor");
                arbeit.setStatus(status != null && !status.isEmpty() ? status : "in Planung");

                int arbeitId = arbeitDAO.create(arbeit);

                // 4. Zeitdaten speichern
                if (startDatum != null || abgabeDatum != null) {
                    Zeitdaten zeit = new Zeitdaten(arbeitId, startDatum, abgabeDatum);
                    zeit.setKolloquiumsdatum(kollDatum);
                    zeitdatenDAO.create(zeit);
                }

                // 5. Betreuer verknüpfen (Notenbestandteil)
                if (!erstprueferName.isEmpty()) {
                    int betreuerId = getOrCreateBetreuer(erstprueferName);
                    Notenbestandteil nb = new Notenbestandteil(arbeitId, betreuerId, "Referent");
                    notenbestandteilDAO.create(nb);
                }
                if (!zweitprueferName.isEmpty()) {
                    int betreuerId = getOrCreateBetreuer(zweitprueferName);
                    Notenbestandteil nb = new Notenbestandteil(arbeitId, betreuerId, "Korreferent");
                    notenbestandteilDAO.create(nb);
                }

                importCount++;
            }
            return "Import erfolgreich! " + importCount + " Datensätze verarbeitet.";
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return "Fehler beim Import: " + e.getMessage();
        }
    }

    // --- Helper Methoden ---

    private int getOrCreateStudent(String matNr, String vorname, String nachname, String email) throws SQLException {
        Optional<Studierende> existing = studierendeDAO.findByMatrikelnummer(matNr);
        if (existing.isPresent()) {
            return existing.get().getStudierendenId();
        }
        Studierende neu = new Studierende(matNr, vorname, nachname, email);
        neu.setAdresse("");
        return studierendeDAO.create(neu);
    }

    private int getOrCreateBetreuer(String fullName) throws SQLException {
        if (betreuerCache.containsKey(fullName)) {
            return betreuerCache.get(fullName);
        }

        // Name parsen: "Prof. Dr. Max Mustermann"
        String[] parts = fullName.split(" ");
        String nachname = parts[parts.length - 1];
        String vorname = parts.length > 1 ? parts[parts.length - 2] : "";
        String titel = "";

        if (fullName.contains("Prof.")) titel = "Prof.";
        if (fullName.contains("Dr.")) titel += (titel.isEmpty() ? "" : " ") + "Dr.";

        // Da wir kein findByName im DAO haben, legen wir im Zweifel neu an (Import-Logik für leere DB)
        // In einer echten App würde man hier erst in der DB suchen.
        Betreuer b = new Betreuer(vorname, nachname, vorname.toLowerCase() + "." + nachname.toLowerCase() + "@thm.de");
        b.setTitel(titel);
        b.setRolle("Professor");

        int id = betreuerDAO.create(b);
        betreuerCache.put(fullName, id);
        return id;
    }

    // --- Default Data Generators ---

    private int getOrCreateDefaultFachbereich() throws SQLException {
        if (!fachbereichDAO.findAll().isEmpty()) return fachbereichDAO.findAll().get(0).getFachbereichId();
        return fachbereichDAO.create(new Fachbereich("Fachbereich MNI", "MNI"));
    }

    private int getOrCreateDefaultStudiengang(int fbId) throws SQLException {
        if (!studiengangDAO.findAll().isEmpty()) return studiengangDAO.findAll().get(0).getStudiengangId();
        return studiengangDAO.create(new Studiengang("Informatik (B.Sc.)", fbId, "INF", "Bachelor of Science", "B.Sc.", true));
    }

    private int getOrCreateDefaultPO(int sgId) throws SQLException {
        if (!poDAO.findAll().isEmpty()) return poDAO.findAll().get(0).getPruefungsordnungId();
        return poDAO.create(new Pruefungsordnung(sgId, "BBPO 2021", LocalDate.of(2021, 1, 1), LocalDate.of(2030, 1, 1), 18, 18));
    }

    private int getOrCreateDefaultSemester() throws SQLException {
        if (!semesterDAO.findAll().isEmpty()) return semesterDAO.findAll().get(0).getSemesterId();

        // Braucht Semesterzeit
        int szId;
        if (!semesterzeitDAO.findAll().isEmpty()) {
            szId = semesterzeitDAO.findAll().get(0).getSemesterZeitId();
        } else {
            szId = semesterzeitDAO.create(new Semesterzeit(LocalDate.of(2024, 10, 1), LocalDate.of(2025, 3, 31), "WiSe 24/25"));
        }

        return semesterDAO.create(new Semester(szId, "Wintersemester 2024/25", "Winter", LocalDate.of(2024, 1, 1)));
    }

    private String getCellVal(Row row, int idx) {
        Cell c = row.getCell(idx);
        return c == null ? "" : c.toString().trim();
    }

    private LocalDate getDateVal(Row row, int idx) {
        Cell c = row.getCell(idx);
        try {
            if (c != null && c.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(c)) {
                return c.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            }
        } catch (Exception e) {
            // Datum kann nicht gelesen werden, ignore
        }
        return null;
    }
}