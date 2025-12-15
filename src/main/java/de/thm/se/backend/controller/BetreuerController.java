package de.thm.se.backend.controller;

import de.thm.se.backend.DataAcessLayer.BetreuerDAO;
import de.thm.se.backend.model.Betreuer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/betreuer")
public class BetreuerController {

    @Autowired
    private BetreuerDAO betreuerDAO;

    @GetMapping
    public List<Betreuer> getAllBetreuer() throws SQLException {
        return betreuerDAO.findAll();
    }

    @GetMapping("/{id}")
    public Betreuer getBetreuer(@PathVariable int id) throws SQLException {
        return betreuerDAO.findById(id).orElse(null);
    }

    @PostMapping
    public Betreuer createBetreuer(@RequestBody Betreuer betreuer) throws SQLException {
        int id = betreuerDAO.create(betreuer);
        betreuer.setBetreuerId(id);
        return betreuer;
    }

    @PutMapping("/{id}")
    public Betreuer updateBetreuer(@PathVariable int id, @RequestBody Betreuer betreuer) throws SQLException {
        betreuer.setBetreuerId(id);
        betreuerDAO.update(betreuer);
        return betreuer;
    }

    @DeleteMapping("/{id}")
    public void deleteBetreuer(@PathVariable int id) throws SQLException {
        betreuerDAO.delete(id);
    }
}