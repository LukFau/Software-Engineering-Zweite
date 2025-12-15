package de.thm.se.backend.controller;

import de.thm.se.backend.DataAcessLayer.ZeitdatenDAO;
import de.thm.se.backend.model.Zeitdaten;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/zeitdaten")
public class ZeitdatenController {

    @Autowired
    private ZeitdatenDAO zeitdatenDAO;

    @GetMapping
    public List<Zeitdaten> getAllZeitdaten() throws SQLException {
        return zeitdatenDAO.findAll();
    }

    @GetMapping("/arbeit/{arbeitId}")
    public Zeitdaten getZeitdatenByArbeitId(@PathVariable int arbeitId) throws SQLException {
        // Hinweis: Das DAO hat keine direkte Methode "findByArbeitId",
        // wir iterieren hier vereinfacht oder du müsstest das DAO erweitern.
        // Für diesen Hack filtern wir die Liste:
        return zeitdatenDAO.findAll().stream()
                .filter(z -> z.getArbeitId() == arbeitId)
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    public Zeitdaten createZeitdaten(@RequestBody Zeitdaten zeitdaten) throws SQLException {
        int id = zeitdatenDAO.create(zeitdaten);
        zeitdaten.setZeitId(id);
        return zeitdaten;
    }

    @PutMapping("/{id}")
    public Zeitdaten updateZeitdaten(@PathVariable int id, @RequestBody Zeitdaten zeitdaten) throws SQLException {
        zeitdaten.setZeitId(id);
        zeitdatenDAO.update(zeitdaten);
        return zeitdaten;
    }
}