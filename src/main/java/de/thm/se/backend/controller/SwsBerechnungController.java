package de.thm.se.backend.controller;

import de.thm.se.backend.DataAcessLayer.SwsBerechnungDAO;
import de.thm.se.backend.model.SwsBerechnung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/sws-berechnungen")
public class SwsBerechnungController {

    @Autowired
    private SwsBerechnungDAO swsBerechnungDAO;

    @GetMapping
    public List<SwsBerechnung> getAllSwsBerechnungen() throws SQLException {
        return swsBerechnungDAO.findAll();
    }

    @GetMapping("/{id}")
    public SwsBerechnung getSwsBerechnung(@PathVariable int id) throws SQLException {
        return swsBerechnungDAO.findById(id).orElse(null);
    }

    @PostMapping
    public SwsBerechnung createSwsBerechnung(@RequestBody SwsBerechnung swsBerechnung) throws SQLException {
        int id = swsBerechnungDAO.create(swsBerechnung);
        swsBerechnung.setSwsId(id);
        return swsBerechnung;
    }

    @PutMapping("/{id}")
    public SwsBerechnung updateSwsBerechnung(@PathVariable int id, @RequestBody SwsBerechnung swsBerechnung) throws SQLException {
        swsBerechnung.setSwsId(id);
        swsBerechnungDAO.update(swsBerechnung);
        return swsBerechnung;
    }

    @DeleteMapping("/{id}")
    public void deleteSwsBerechnung(@PathVariable int id) throws SQLException {
        swsBerechnungDAO.delete(id);
    }
}