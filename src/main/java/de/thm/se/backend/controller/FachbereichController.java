package de.thm.se.backend.controller;

import de.thm.se.backend.DataAcessLayer.FachbereichDAO;
import de.thm.se.backend.model.Fachbereich;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/fachbereiche")
public class FachbereichController {

    @Autowired
    private FachbereichDAO fachbereichDAO;

    @GetMapping
    public List<Fachbereich> getAllFachbereiche() throws SQLException {
        return fachbereichDAO.findAll();
    }

    @GetMapping("/{id}")
    public Fachbereich getFachbereich(@PathVariable int id) throws SQLException {
        return fachbereichDAO.findById(id).orElse(null);
    }

    @PostMapping
    public Fachbereich createFachbereich(@RequestBody Fachbereich fachbereich) throws SQLException {
        int id = fachbereichDAO.create(fachbereich);
        fachbereich.setFachbereichId(id);
        return fachbereich;
    }

    @PutMapping("/{id}")
    public Fachbereich updateFachbereich(@PathVariable int id, @RequestBody Fachbereich fachbereich) throws SQLException {
        fachbereich.setFachbereichId(id);
        fachbereichDAO.update(fachbereich);
        return fachbereich;
    }

    @DeleteMapping("/{id}")
    public void deleteFachbereich(@PathVariable int id) throws SQLException {
        fachbereichDAO.delete(id);
    }
}