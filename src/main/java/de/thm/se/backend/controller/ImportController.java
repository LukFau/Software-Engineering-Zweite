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
import java.util.Date;

@RestController
@RequestMapping("/api/import")
public class ImportController {

    @Autowired private StudierendeDAO studierendeDAO;
    @Autowired private WissenschaftlicheArbeitDAO arbeitDAO;
    @Autowired private BetreuerDAO betreuerDAO;
    @Autowired private ZeitdatenDAO zeitdatenDAO;
    // Weitere DAOs wie StudiengangDAO hier einbinden...

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // Überspringe Header-Zeile (i=1 statt i=0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // 1. Daten aus Excel lesen (Spaltenindexe basierend auf deinem Mockup)
                // Beispiel Mapping:
                // A: Status, D: Erstprüfer, F: Nachname, G: Vorname, I: Matrikelnr, K: Studiengang, L: Titel
                // M: Start, N: Abgabe

                String status = getCellVal(row, 0);
                String email = getCellVal(row, 2);
                String erstprueferName = getCellVal(row, 3);
                String nachname = getCellVal(row, 5);
                String vorname = getCellVal(row, 6);
                String matrikelnr = getCellVal(row, 8); // Achtung: Spaltenindex prüfen
                String studiengangBez = getCellVal(row, 10);
                String titel = getCellVal(row, 11);
                LocalDate start = getDateVal(row, 12);
                LocalDate ende = getDateVal(row, 13);

                // 2. Student speichern oder finden
                Studierende student = new Studierende(matrikelnr, vorname, nachname, email);
                // Hier Logik einbauen: Prüfen ob Student existiert, sonst create
                int studentId = studierendeDAO.create(student);

                // 3. Arbeit anlegen
                // Dummy-Werte für IDs, die noch gesucht werden müssten (Studiengang, PO, Semester)
                WissenschaftlicheArbeit arbeit = new WissenschaftlicheArbeit();
                arbeit.setStudierendenId(studentId);
                arbeit.setTitel(titel);
                arbeit.setStatus(status);
                arbeit.setTyp("Bachelor"); // oder aus Excel lesen
                arbeit.setStudiengangId(1); // TODO: Echten Studiengang suchen
                arbeit.setPruefungsordnungId(1); // TODO: Echte PO suchen
                arbeit.setSemesterId(1); // TODO: Echtes Semester suchen

                int arbeitId = arbeitDAO.create(arbeit);

                // 4. Zeitdaten anlegen
                if (start != null || ende != null) {
                    Zeitdaten zeiten = new Zeitdaten(arbeitId, start, ende);
                    zeitdatenDAO.create(zeiten);
                }
            }
            return "Import erfolgreich!";
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return "Fehler beim Import: " + e.getMessage();
        }
    }

    // Hilfsmethoden für Excel
    private String getCellVal(Row row, int cellIdx) {
        Cell cell = row.getCell(cellIdx);
        if (cell == null) return "";
        return cell.toString();
    }

    private LocalDate getDateVal(Row row, int cellIdx) {
        Cell cell = row.getCell(cellIdx);
        if (cell != null && cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }
}