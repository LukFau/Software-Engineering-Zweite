package de.thm.se.backend.controller;

import de.thm.se.backend.DataAcessLayer.WissenschaftlicheArbeitDAO;
import de.thm.se.backend.model.ArbeitMitDetails;
import de.thm.se.backend.model.WissenschaftlicheArbeit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/wissenschaftliche-arbeiten")
public class WissenschaftlicheArbeitController {

    @Autowired
    private WissenschaftlicheArbeitDAO wissenschaftlicheArbeitDAO;

    @GetMapping
    public List<WissenschaftlicheArbeit> getAllWissenschaftlicheArbeiten() throws SQLException {
        return wissenschaftlicheArbeitDAO.findAll();
    }

    @GetMapping("/{id}")
    public WissenschaftlicheArbeit getWissenschaftlicheArbeit(@PathVariable int id) throws SQLException {
        return wissenschaftlicheArbeitDAO.findById(id).orElse(null);
    }

    @GetMapping("/studierender/{studierendenId}")
    public List<WissenschaftlicheArbeit> getArbeitenByStudierendenId(@PathVariable int studierendenId) throws SQLException {
        return wissenschaftlicheArbeitDAO.findByStudierendenId(studierendenId);
    }

    @GetMapping("/status/{status}")
    public List<WissenschaftlicheArbeit> getArbeitenByStatus(@PathVariable String status) throws SQLException {
        return wissenschaftlicheArbeitDAO.findByStatus(status);
    }

    @GetMapping("/details")
    public List<ArbeitMitDetails> getAlleArbeitenMitDetails() throws SQLException {
        return wissenschaftlicheArbeitDAO.findAlleMitDetails();
    }

    @PostMapping
    public WissenschaftlicheArbeit createWissenschaftlicheArbeit(@RequestBody WissenschaftlicheArbeit arbeit) throws SQLException {
        int id = wissenschaftlicheArbeitDAO.create(arbeit);
        arbeit.setArbeitId(id);
        return arbeit;
    }

    @PutMapping("/{id}")
    public WissenschaftlicheArbeit updateWissenschaftlicheArbeit(@PathVariable int id, @RequestBody WissenschaftlicheArbeit arbeit) throws SQLException {
        arbeit.setArbeitId(id);
        wissenschaftlicheArbeitDAO.update(arbeit);
        return arbeit;
    }

    @PutMapping("/{id}/status")
    public boolean updateStatus(@PathVariable int id, @RequestParam String neuerStatus) throws SQLException {
        return wissenschaftlicheArbeitDAO.updateStatus(id, neuerStatus);
    }

    @DeleteMapping("/{id}")
    public void deleteWissenschaftlicheArbeit(@PathVariable int id) throws SQLException {
        wissenschaftlicheArbeitDAO.delete(id);
    }
}