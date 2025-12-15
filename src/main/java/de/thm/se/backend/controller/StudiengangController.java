package de.thm.se.backend.controller;

import de.thm.se.backend.DataAcessLayer.StudiengangDAO;
import de.thm.se.backend.model.Studiengang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/studiengaenge")
public class StudiengangController {

    @Autowired
    private StudiengangDAO studiengangDAO;

    @GetMapping
    public List<Studiengang> getAllStudiengaenge() throws SQLException {
        return studiengangDAO.findAll();
    }

    @GetMapping("/{id}")
    public Studiengang getStudiengang(@PathVariable int id) throws SQLException {
        return studiengangDAO.findById(id).orElse(null);
    }

    @PostMapping
    public Studiengang createStudiengang(@RequestBody Studiengang studiengang) throws SQLException {
        int id = studiengangDAO.create(studiengang);
        studiengang.setStudiengangId(id);
        return studiengang;
    }

    @PutMapping("/{id}")
    public Studiengang updateStudiengang(@PathVariable int id, @RequestBody Studiengang studiengang) throws SQLException {
        studiengang.setStudiengangId(id);
        studiengangDAO.update(studiengang);
        return studiengang;
    }

    @DeleteMapping("/{id}")
    public void deleteStudiengang(@PathVariable int id) throws SQLException {
        studiengangDAO.delete(id);
    }
}