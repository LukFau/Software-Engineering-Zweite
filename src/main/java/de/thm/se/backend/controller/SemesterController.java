package de.thm.se.backend.controller;

import de.thm.se.backend.DataAcessLayer.SemesterDAO;
import de.thm.se.backend.model.Semester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/semester")
public class SemesterController {

    @Autowired
    private SemesterDAO semesterDAO;

    @GetMapping
    public List<Semester> getAllSemester() throws SQLException {
        return semesterDAO.findAll();
    }

    @GetMapping("/{id}")
    public Semester getSemester(@PathVariable int id) throws SQLException {
        return semesterDAO.findById(id).orElse(null);
    }

    @PostMapping
    public Semester createSemester(@RequestBody Semester semester) throws SQLException {
        int id = semesterDAO.create(semester);
        semester.setSemesterId(id);
        return semester;
    }

    @PutMapping("/{id}")
    public Semester updateSemester(@PathVariable int id, @RequestBody Semester semester) throws SQLException {
        semester.setSemesterId(id);
        semesterDAO.update(semester);
        return semester;
    }

    @DeleteMapping("/{id}")
    public void deleteSemester(@PathVariable int id) throws SQLException {
        semesterDAO.delete(id);
    }
}