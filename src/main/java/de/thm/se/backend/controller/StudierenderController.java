package de.thm.se.backend.controller;

import de.thm.se.backend.DataAcessLayer.StudierendeDAO;
import de.thm.se.backend.model.Studierende;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("api/studierende")
public class StudierenderController {

    @Autowired
    private StudierendeDAO studierendeDAO;

    @GetMapping
    public List<Studierende> getAllStudierende() throws SQLException {
        return studierendeDAO.findAll();
    }

    @GetMapping("/{id}")
    public Studierende getStudierender(@PathVariable int id) throws SQLException {
        return studierendeDAO.findById(id).orElse(null);
    }

    @PostMapping
    public Studierende creatStudierende(@RequestBody Studierende studierende) throws SQLException {
        int id = studierendeDAO.create(studierende);
        studierende.setStudierendenId(id);
        return studierende;
    }

    @PutMapping("/{id}")
    public Studierende updateStudierende(@PathVariable int id, @RequestBody Studierende studierender) throws SQLException {
        studierender.setStudierendenId(id);
        studierendeDAO.update(studierender);
        return studierender;
    }

    @DeleteMapping("/{id}")
    public void deleteStudierender(@PathVariable int id) throws SQLException {
        studierendeDAO.delete(id);
    }
}
