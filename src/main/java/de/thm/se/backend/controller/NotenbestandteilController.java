package de.thm.se.backend.controller;

import de.thm.se.backend.DataAcessLayer.NotenbestandteilDAO;
import de.thm.se.backend.model.Notenbestandteil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/notenbestandteile")
public class NotenbestandteilController {

    @Autowired
    private NotenbestandteilDAO notenbestandteilDAO;

    @GetMapping
    public List<Notenbestandteil> getAllNotenbestandteile() throws SQLException {
        return notenbestandteilDAO.findAll();
    }

    @GetMapping("/{id}")
    public Notenbestandteil getNotenbestandteil(@PathVariable int id) throws SQLException {
        return notenbestandteilDAO.findById(id).orElse(null);
    }

    @PostMapping
    public Notenbestandteil createNotenbestandteil(@RequestBody Notenbestandteil notenbestandteil) throws SQLException {
        int id = notenbestandteilDAO.create(notenbestandteil);
        notenbestandteil.setNotenId(id);
        return notenbestandteil;
    }

    @PutMapping("/{id}")
    public Notenbestandteil updateNotenbestandteil(@PathVariable int id, @RequestBody Notenbestandteil notenbestandteil) throws SQLException {
        notenbestandteil.setNotenId(id);
        notenbestandteilDAO.update(notenbestandteil);
        return notenbestandteil;
    }

    @DeleteMapping("/{id}")
    public void deleteNotenbestandteil(@PathVariable int id) throws SQLException {
        notenbestandteilDAO.delete(id);
    }
}