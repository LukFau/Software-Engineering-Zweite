package de.thm.se.backend.controller;

import de.thm.se.backend.DataAcessLayer.PruefungsordnungDAO;
import de.thm.se.backend.model.Pruefungsordnung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/pruefungsordnungen")
public class PruefungsordnungController {

    @Autowired
    private PruefungsordnungDAO pruefungsordnungDAO;

    @GetMapping
    public List<Pruefungsordnung> getAllPruefungsordnungen() throws SQLException {
        return pruefungsordnungDAO.findAll();
    }

    @GetMapping("/{id}")
    public Pruefungsordnung getPruefungsordnung(@PathVariable int id) throws SQLException {
        return pruefungsordnungDAO.findById(id).orElse(null);
    }

    @PostMapping
    public Pruefungsordnung createPruefungsordnung(@RequestBody Pruefungsordnung pruefungsordnung) throws SQLException {
        int id = pruefungsordnungDAO.create(pruefungsordnung);
        pruefungsordnung.setPruefungsordnungId(id);
        return pruefungsordnung;
    }

    @PutMapping("/{id}")
    public Pruefungsordnung updatePruefungsordnung(@PathVariable int id, @RequestBody Pruefungsordnung pruefungsordnung) throws SQLException {
        pruefungsordnung.setPruefungsordnungId(id);
        pruefungsordnungDAO.update(pruefungsordnung);
        return pruefungsordnung;
    }

    @DeleteMapping("/{id}")
    public void deletePruefungsordnung(@PathVariable int id) throws SQLException {
        pruefungsordnungDAO.delete(id);
    }
}