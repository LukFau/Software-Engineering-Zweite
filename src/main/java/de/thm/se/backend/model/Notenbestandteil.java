package de.thm.se.backend.model;

/**
 * Model-Klasse für Notenbestandteile
 * Entspricht der Tabelle NOTENBESTANDTEIL
 */

public class Notenbestandteil {
    private int notenId;
    private int arbeitId;
    private int betreuerId;
    private String rolle; // "Referent" oder "Korreferent"
    private double noteArbeit;
    private double noteKolloquium;
    private double gewichtung;

    // Konstruktoren
    public Notenbestandteil() {
    }

    public Notenbestandteil(int arbeitId, int betreuerId, String rolle) {
        this.arbeitId = arbeitId;
        this.betreuerId = betreuerId;
        this.rolle = rolle;
        this.gewichtung = rolle.equals("Referent") ? 0.75 : 0.25;
    }

    // Getter und Setter
    public int getNotenId() {
        return notenId;
    }

    public void setNotenId(int notenId) {
        this.notenId = notenId;
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

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    public double getNoteArbeit() {
        return noteArbeit;
    }

    public void setNoteArbeit(double noteArbeit) {
        this.noteArbeit = noteArbeit;
    }

    public double getNoteKolloquium() {
        return noteKolloquium;
    }

    public void setNoteKolloquium(double noteKolloquium) {
        this.noteKolloquium = noteKolloquium;
    }

    public double getGewichtung() {
        return gewichtung;
    }

    public void setGewichtung(double gewichtung) {
        this.gewichtung = gewichtung;
    }

    // Berechnungsmethoden
    public double berechneGesamtnote() {
        return (noteArbeit * 0.7 + noteKolloquium * 0.3); // 70% Arbeit und 30% Kolloquium
    }

    public double berechneGewichteteNote() {
        return berechneGesamtnote() * gewichtung;
    }

    // Hilfsmethoden
    @Override
    public String toString() {
        return String.format("Note[%s: Arbeit=%.1f, Kolloquium=%.1f, Gewichtung=%.of%%]", rolle, noteArbeit,
                noteKolloquium, gewichtung * 100);
    }
}
