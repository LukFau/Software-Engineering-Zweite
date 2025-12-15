package de.thm.se.backend.model;

import java.time.LocalDate;

/**
 * Model-Klasse für SemesterZeit
 * Entspricht der Tabelle SEMESTERZEIT
 */
public class Semesterzeit {
    private int semesterZeitId;
    private LocalDate beginn;
    private LocalDate ende;
    private String bezeichnung;

    // Konstruktoren
    public Semesterzeit() {
    }

    public Semesterzeit(LocalDate beginn, LocalDate ende, String bezeichnung) {
        this.beginn = beginn;
        this.ende = ende;
        this.bezeichnung = bezeichnung;
    }

    // Getter und Setter
    public int getSemesterZeitId() {
        return semesterZeitId;
    }

    public void setSemesterZeitId(int semesterZeitId) {
        this.semesterZeitId = semesterZeitId;
    }

    public LocalDate getBeginn() {
        return beginn;
    }

    public void setBeginn(LocalDate beginn) {
        this.beginn = beginn;
    }

    public LocalDate getEnde() {
        return ende;
    }

    public void setEnde(LocalDate ende) {
        this.ende = ende;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    // Hilfsmethoden
    @Override
    public String toString() {
        return String.format("SemesterZeit[ID=%d, Bezeichnung=&s", semesterZeitId, bezeichnung);
    }
}
