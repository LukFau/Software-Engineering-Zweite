package de.thm.se.backend.model;

import java.time.LocalDate;

/**
 * Model-Klasse für SWS Berechnung
 * Entspricht der Tabelle SWS_BERECHNUNG
 */
public class SwsBerechnung {
    private int swsId;
    private int arbeitId;
    private int betreuerId;
    private int semesterId;
    private int pruefungsordnungId;
    private float swsWert;
    private String rolle;
    private LocalDate berechnetAm;

    // Konstruktor
    public SwsBerechnung() {
    }

    public SwsBerechnung(int arbeitId, int betreuerId, int semesterId, int pruefungsordnungId) {
        this.arbeitId = arbeitId;
        this.betreuerId = betreuerId;
        this.semesterId = semesterId;
        this.pruefungsordnungId = pruefungsordnungId;
    }

    // Getter und Setter
    public int getSwsId() {
        return swsId;
    }

    public void setSwsId(int swsId) {
        this.swsId = swsId;
    }

    public int getArbeitId() {
        return arbeitId;
    }

    public void setArbeitId(int arbeitId) {
        this.arbeitId = arbeitId;
    }

    public int getBetreuerId() {
        return betreuerId;
    }

    public void setBetreuerId(int betreuerId) {
        this.betreuerId = betreuerId;
    }

    public int getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(int semesterId) {
        this.semesterId = semesterId;
    }

    public int getPruefungsordnungId() {
        return pruefungsordnungId;
    }

    public void setPruefungsordnungId(int pruefungsordnungId) {
        this.pruefungsordnungId = pruefungsordnungId;
    }

    public float getSwsWert() {
        return swsWert;
    }

    public void setSwsWert(float swsWert) {
        this.swsWert = swsWert;
    }

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    public LocalDate getBerechnetAm() {
        return berechnetAm;
    }

    public void setBerechnetAm(LocalDate berechnetAm) {
        this.berechnetAm = berechnetAm;
    }

    // Hilfsmethoden
    @Override
    public String toString() {
        return String.format(
                "SWSBerechnung[ID=%d, ArbeitID=%d, BetreuerID=%d, SemesterID=%d, PrüfungsordnungID=%d, SWSWert=%f]",
                swsId, arbeitId, betreuerId, semesterId, pruefungsordnungId, swsWert);
    }

}
