package de.thm.se.backend.controller;

import de.thm.se.backend.DataAcessLayer.*;
import de.thm.se.backend.model.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/import")
public class ImportController {

    @Autowired private StudierendeDAO studierendeDAO;
    @Autowired private WissenschaftlicheArbeitDAO arbeitDAO;
    @Autowired private BetreuerDAO betreuerDAO;
    @Autowired private ZeitdatenDAO zeitdatenDAO;
    @Autowired private StudiengangDAO studiengangDAO;
    @Autowired private SemesterDAO semesterDAO;
    @Autowired private PruefungsordnungDAO poDAO;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            // Wir gehen davon aus, dass die relevanten Daten im Reiter "Arbeiten" oder im ersten Blatt sind
            Sheet sheet = workbook.getSheet("Arbeiten");
            if (sheet == null) sheet = workbook.getSheetAt(0);

            int importCount = 0;

            // Iteriere über Zeilen (Start bei 1, um Header zu überspringen)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // 1. Daten aus Excel lesen (Spaltenindexe basierend auf Mockup)
                String status = getCellVal(row, 0);       // A: Status
                String email = getCellVal(row, 2);        // C: Kontakt
                String erstprueferName = getCellVal(row, 3); // D: Erstprüfer
                String zweitprueferName = getCellVal(row, 4); // E: Zweitprüfer
                String nachname = getCellVal(row, 5);     // F: Nachname
                String vorname = getCellVal(row, 6);      // G: Vorname
                String matrikelnr = getCellVal(row, 8);   // I: Matrikelnr
                String studiengangTxt = getCellVal(row, 10); // K: Studiengang
                String titel = getCellVal(row, 11);       // L: Titel

                LocalDate startDatum = getDateVal(row, 12); // M: Start
                LocalDate abgabeDatum = getDateVal(row, 13); // N: Abgabe
                LocalDate kollDatum = getDateVal(row, 15);   // P: Kolloquium

                if (titel.isEmpty() || matrikelnr.isEmpty()) continue;

                // 2. Studierenden verarbeiten (Finden oder Erstellen)
                int studentId = getOrCreateStudent(matrikelnr, vorname, nachname, email);

                // 3. Studiengang finden (Vereinfacht: Wir nehmen den ersten, falls Text passt, sonst Dummy)
                // In einer echten App müsste man hier 'studiengangTxt' gegen die DB prüfen.
                int studiengangId = 1; // Fallback ID

                // 4. Semester und PO (Dummy-Werte für Import, da Logik komplexer wäre)
                int semesterId = 1;
                int poId = 1;

                // 5. Arbeit anlegen
                WissenschaftlicheArbeit arbeit = new WissenschaftlicheArbeit();
                arbeit.setStudierendenId(studentId);
                arbeit.setStudiengangId(studiengangId);
                arbeit.setPruefungsordnungId(poId);
                arbeit.setSemesterId(semesterId);
                arbeit.setTitel(titel);
                arbeit.setTyp("Bachelor"); // Annahme, könnte man aus Studiengang ableiten
                arbeit.setStatus(status);

                int arbeitId = arbeitDAO.create(arbeit);

                // 6. Zeitdaten speichern
                if (startDatum != null || abgabeDatum != null) {
                    Zeitdaten zeit = new Zeitdaten(arbeitId, startDatum, abgabeDatum);
                    zeit.setKolloquiumsdatum(kollDatum);
                    zeitdatenDAO.create(zeit);
                }

                // 7. Betreuer verarbeiten (Erstprüfer)
                if (!erstprueferName.isEmpty()) {
                    createNotenbestandteilForBetreuer(arbeitId, erstprueferName, "Referent");
                }
                // 8. Betreuer verarbeiten (Zweitprüfer)
                if (!zweitprueferName.isEmpty()) {
                    createNotenbestandteilForBetreuer(arbeitId, zweitprueferName, "Korreferent");
                }

                importCount++;
            }
            return "Import erfolgreich! " + importCount + " Datensätze verarbeitet.";
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return "Fehler beim Import: " + e.getMessage();
        }
    }

    // --- Hilfsmethoden ---

    private int getOrCreateStudent(String matNr, String vorname, String nachname, String email) throws SQLException {
        Optional<Studierende> existing = studierendeDAO.findByMatrikelnummer(matNr);
        if (existing.isPresent()) {
            return existing.get().getStudierendenId();
        }
        Studierende neu = new Studierende(matNr, vorname, nachname, email);
        neu.setAdresse(""); // Default
        return studierendeDAO.create(neu);
    }

    private void createNotenbestandteilForBetreuer(int arbeitId, String name, String rolle) throws SQLException {
        // Name parsen (z.B. "Prof. Dr. Max Mustermann")
        // Sehr vereinfachte Logik: Letztes Wort = Nachname, Rest = Vorname/Titel
        String[] parts = name.split(" ");
        String nachname = parts[parts.length-1];
        String vorname = parts.length > 1 ? parts[parts.length-2] : "";
        // Besser wäre eine Suche in der DB nach dem Betreuer

        // Hier vereinfacht: Wir suchen nicht, sondern legen im Zweifel (oder Demo) nicht an,
        // da wir keine Betreuer-Suche per Name im DAO haben.
        // TODO: BetreuerDAO.findByName implementieren und hier nutzen.
    }

    private String getCellVal(Row row, int idx) {
        Cell c = row.getCell(idx);
        return c == null ? "" : c.toString();
    }

    private LocalDate getDateVal(Row row, int idx) {
        Cell c = row.getCell(idx);
        if (c != null && c.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(c)) {
            return c.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }
}