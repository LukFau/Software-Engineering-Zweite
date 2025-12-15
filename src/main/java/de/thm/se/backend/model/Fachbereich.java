package de.thm.se.backend.model;

/**
 * Model-Klasse für Fachbereiche
 * Entspricht der Tabelle FACHBEREICH
 */
public class Fachbereich {
    private int fachbereichId;
    private String bezeichnung;
    private String fbname;

    // Konstruktoren
    public Fachbereich() {
    }

    public Fachbereich(String bezeichnung, String fbname) {
        this.bezeichnung = bezeichnung;
        this.fbname = fbname;
    }

    // Getter und Setter
    public int getFachbereichId() {
        return fachbereichId;
    }

    public void setFachbereichId(int fachbereichId) {
        this.fachbereichId = fachbereichId;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getFbname() {
        return fbname;
    }

    public void setFbname(String fbname) {
        this.fbname = fbname;
    }

    // Hilfsmethoden
    @Override
    public String toString() {
        return String.format("Fachbereich[ID=%d, Bezeichnung=%s]", fachbereichId, bezeichnung);
    }
}
