package de.thm.se.backend.model;

import java.time.LocalDate;

/**
 * Model-Klasse Semester
 * Entspricht der Tabelle SEMESTER
 */
public class Semester {
    private int semesterId;
    private int semesterzeitId;
    private String bezeichnung;
    private String typ;
    private LocalDate jahr;
    
    //Konstruktor
    public Semester(){
    }
    
    public Semester(int semesterZeitId, String bezeichnung, String typ, LocalDate jahr) {
        this.semesterzeitId = semesterZeitId;
        this.bezeichnung = bezeichnung;
        this.typ = typ;
        this.jahr = jahr;
    }
    
    //Getter und Setter
    public int getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(int semesterId) {
        this.semesterId = semesterId;
    }

    public int getSemesterzeitId() {
        return semesterzeitId;
    }

    public void setSemesterzeitId(int semesterZeitId) {
        this.semesterzeitId = semesterZeitId;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public LocalDate getJahr() {
        return jahr;
    }

    public void setJahr(LocalDate jahr) {
        this.jahr = jahr;
    }

    //Hilfsmethoden
    @Override
    public String toString() {
        return "Semester [semesterId=" + semesterId + ", bezeichnung=" + bezeichnung + ", typ=" + typ + ", jahr=" + jahr
                + "]";
    }

}
