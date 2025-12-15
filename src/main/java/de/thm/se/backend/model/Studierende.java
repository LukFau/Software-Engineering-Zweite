package de.thm.se.backend.model;

import java.time.LocalDate;

/**
 * Model-Klasse für Studierende
 * Entspricht der Tabelle STUDIERENDE in der Datenbank
 */

public class Studierende {
    private int studierendenId;
    private String matrikelnummer;
    private String vorname;
    private String nachname;
    private String email;
    private LocalDate geburtsdatum;
    private String adresse;

    // Konstruktor
    public Studierende() {
    }

    public Studierende(String matrikelnummer, String vorname, String nachname, String email) {
        this.matrikelnummer = matrikelnummer;
        this.vorname = vorname;
        this.nachname = nachname;
        this.email = email;
    }

    // Getter und Setter
    public int getStudierendenId() {
        return studierendenId;
    }

    public void setStudierendenId(int studierendenId) {
        this.studierendenId = studierendenId;
    }

    public String getMatrikelnummer() {
        return matrikelnummer;
    }

    public void setMatrikelnummer(String matrikelnummer) {
        this.matrikelnummer = matrikelnummer;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(LocalDate geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    // Hilfsmethoden
    @Override
    public String toString() {
        return String.format("Studierender [ID=%d, Matrikel=%s, Name=%s %s, Email=%s]", studierendenId, matrikelnummer,
                vorname, nachname, email);
    }

    public String getVollstaendigerName() {
        return vorname + " " + nachname;
    }

}
